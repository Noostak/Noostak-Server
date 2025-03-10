package org.noostak.auth.application.kakao;


import lombok.RequiredArgsConstructor;
import org.noostak.auth.application.RestClient;
import org.noostak.auth.common.exception.AuthErrorCode;
import org.noostak.auth.common.exception.AuthException;
import org.noostak.auth.domain.vo.AuthId;
import org.noostak.auth.dto.kakao.*;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaoServiceImpl implements KakaoService {

    private final RestClient restClient;

    @Override
    public AuthId verifyByAuthAccessToken(String accessToken) {
        String url = KaKaoApi.USER_INFO.getUrl();

        HttpHeaders headers = makeAuthorizationBearerTokenHeader(accessToken);

        KakaoUserInfoResponse response =
                restClient.postRequest(url, headers, KakaoUserInfoResponse.class);

        response.validate();

        return AuthId.from(response.getId());
    }

    @Override
    public void logout(String accessToken) {
        // 별도로 처리하지 않음
    }

    @Override
    public void unlink(String accessToken) {
        // 별도로 처리하지 않음
    }

    public HttpHeaders makeAuthorizationBearerTokenHeader(String token) {
        HttpHeaders headers = new HttpHeaders();

        if (token == null || token.isEmpty() || token.isBlank()) {
            throw new AuthException(AuthErrorCode.INVALID_TOKEN);
        }

        headers.set("Authorization", "Bearer " + token);

        return headers;
    }
}
