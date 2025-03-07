package org.noostak.auth.application.apple;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.Jwts;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import org.noostak.auth.common.exception.AppleDecodeErrorCode;
import org.noostak.auth.common.exception.AppleDecodeException;
import org.noostak.auth.common.exception.AuthErrorCode;
import org.noostak.auth.common.exception.AuthException;
import org.noostak.auth.dto.apple.AppleKeyResponse;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class AppleTokenVerifier {

    public static String verifyIdToken(String idToken) {
        try {
            // 1. JWT 헤더, 페이로드 디코딩
            Map<String, String> algAndKid = getAlgAndKidFromIdToken(idToken);
            String kid = algAndKid.get("kid");
            String alg = algAndKid.get("alg");

            // 2. Apple 공개키 가져오기
            PublicKey publicKey = getApplePublicKey(kid, alg);

            // 3. JWT 서명 검증
            Claims claims = Jwts.parser()
                    .verifyWith(publicKey)
                    .build()
                    .parseSignedClaims(idToken)
                    .getPayload();

            return claims.getSubject();
        }catch (Exception e){
            throw new AppleDecodeException(AppleDecodeErrorCode.DECODE_ERROR,e.getMessage());
        }
    }

    private static Map<String, String> getAlgAndKidFromIdToken(String idToken) throws JsonProcessingException {
        Map<String, String> algAndKid = new HashMap<>();

        // ID 토큰을 `.` 기준으로 분할 (헤더.페이로드.서명)
        String header = idToken.split("\\.")[0];

        // Base64 URL 디코딩
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String headerJson = new String(decoder.decode(header));

        // JSON 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> headerContent = objectMapper.readValue(headerJson, Map.class);

        // `alg` 및 `kid` 값 추출
        algAndKid.put("alg", (String) headerContent.getOrDefault("alg", ""));
        algAndKid.put("kid", (String) headerContent.getOrDefault("kid", ""));

        return algAndKid;
    }

    private static PublicKey getApplePublicKey(String kid, String alg) throws Exception {
        // Apple 공개키 요청 및 파싱 로직

        AppleKeyResponse.RsaKey applePublicKeyObj = fetchApplePublicKey(kid, alg);
        return generatePublicKey(applePublicKeyObj);
    }

    private static AppleKeyResponse.RsaKey fetchApplePublicKey(String kid, String alg) {
        // 키 후보들과 identity token에서 뽑아낸 alg, kid를 비교해 매칭되는 키를 찾는 메서드

        AppleKeyResponse response =
                new RestTemplate().getForObject(AppleApi.PUBLIC_KEY.getUrl(), AppleKeyResponse.class);

        for (AppleKeyResponse.RsaKey key : response.getKeys()) {
            String algorithm = key.getAlgorithm();
            String givenKeyId = key.getKeyId();

            if (givenKeyId.equals(kid) && algorithm.equals(alg)) {
                return key;
            }
        }

        throw new AuthException(AuthErrorCode.INVALID_TOKEN);
    }

    private static PublicKey generatePublicKey(AppleKeyResponse.RsaKey applePublicKeyObj) throws InvalidKeySpecException, NoSuchAlgorithmException {
        // 찾아낸 후보 키를 공개키 인스턴스로 만드는 메서드

        String kty = applePublicKeyObj.getKeyType();
        byte[] modulusBytes = Base64.getUrlDecoder().decode(applePublicKeyObj.getModulus());
        byte[] exponentBytes = Base64.getUrlDecoder().decode(applePublicKeyObj.getExponent());

        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(
                new BigInteger(1, modulusBytes),
                new BigInteger(1, exponentBytes)
        );


        KeyFactory keyFactory = KeyFactory.getInstance(kty);
        return keyFactory.generatePublic(publicKeySpec);
    }
}