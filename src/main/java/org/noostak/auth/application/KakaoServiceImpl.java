package org.noostak.auth.application;


import lombok.RequiredArgsConstructor;
import org.noostak.auth.application.jwt.JwtToken;
import org.noostak.auth.application.jwt.JwtTokenProvider;
import org.noostak.auth.common.exception.AuthErrorCode;
import org.noostak.auth.common.exception.AuthException;
import org.noostak.auth.common.exception.ExternalApiException;
import org.noostak.auth.domain.vo.AuthId;
import org.noostak.auth.dto.*;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaoServiceImpl implements KakaoService{

    private final KakaoTokenRequestFactory kakaoTokenRequestFactory;
    private final RestClient restClient;
    @Override
    public JwtToken requestAccessToken(String givenRefreshToken) throws ExternalApiException {
        String url = KaKaoApi.TOKEN_REQUEST.getUrl();

        KakaoAccessTokenRequest request =
                kakaoTokenRequestFactory.createAccessTokenRequest(givenRefreshToken);

        KakaoAccessTokenResponse response =
                restClient.postRequest(url,
                        request.getUrlEncodedParams(),
                        KakaoAccessTokenResponse.class);

        response.validate();

        return JwtTokenProvider.createToken(response.getAccessToken(), response.getRefreshToken());
    }


    @Override
    public JwtToken requestToken(String code) {
        String url = KaKaoApi.TOKEN_REQUEST.getUrl();

        KakaoTokenRequest request = kakaoTokenRequestFactory.createRequest(code);

        KakaoTokenResponse response =
                restClient.postRequest(url,
                        request.getUrlEncodedParams(),
                        KakaoTokenResponse.class);

        response.validate();

        return JwtTokenProvider.createToken(response.getAccessToken(),response.getRefreshToken());
    }

    @Override
    public AuthId verify(String accessToken) {
        String url = KaKaoApi.USER_INFO.getUrl();

        HttpHeaders headers = makeAuthorizationBearerTokenHeader(accessToken);

        KakaoUserInfoResponse response =
                restClient.postRequest(url, headers, KakaoUserInfoResponse.class);

        response.validate();

        return AuthId.from(response.getId());
    }

    public HttpHeaders makeAuthorizationBearerTokenHeader(String token){
        HttpHeaders headers = new HttpHeaders();

        if(token == null || token.isEmpty() || token.isBlank()){
            throw new AuthException(AuthErrorCode.INVALID_TOKEN);
        }

        headers.set("Authorization", "Bearer " + token);

        return headers;
    }
}
