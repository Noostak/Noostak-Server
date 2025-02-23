package org.noostak.auth.application.jwt;


import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtTokenProvider {
    private final SecretKey secretKey;

    public JwtTokenProvider(String secretKey) {
        this.secretKey = Jwts.SIG.HS256.key().build();
    }

    public static JwtToken createToken(String accessToken, String refreshToken){
        return new JwtToken("Bearer",accessToken,refreshToken);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public String getAuthId(String token) {

        // TODO: 카카오, 구글 별로 액세스 토큰에서 AuthId 추출하기
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}