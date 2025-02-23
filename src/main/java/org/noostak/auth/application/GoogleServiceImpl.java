package org.noostak.auth.application;


import lombok.RequiredArgsConstructor;
import org.noostak.auth.application.jwt.JwtToken;
import org.noostak.auth.application.jwt.JwtTokenProvider;
import org.noostak.auth.common.exception.AuthErrorCode;
import org.noostak.auth.common.exception.AuthException;
import org.noostak.auth.domain.vo.AuthId;
import org.noostak.auth.dto.*;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoogleServiceImpl implements GoogleService{

    private final GoogleTokenRequestFactory googleTokenRequestFactory;
    private final RestClient restClient;

    @Override
    public JwtToken requestAccessToken(String givenRefreshToken) {
        String url = GoogleApi.TOKEN_REQUEST.getUrl();

        GoogleAccessTokenRequest request =
                googleTokenRequestFactory.createAccessTokenRequest(givenRefreshToken);

        GoogleAccessTokenResponse response =
                restClient.postRequest(url,
                        request.getUrlEncodedParams(),
                        GoogleAccessTokenResponse.class);

        response.validate();

        return JwtTokenProvider.createToken(response.getAccessToken(), response.getRefreshToken());
    }

    @Override
    public JwtToken requestToken(String code) {
        String url = GoogleApi.TOKEN_REQUEST.getUrl();

        GoogleTokenRequest request = googleTokenRequestFactory.createRequest(code);

        GoogleTokenResponse response =
                restClient.postRequest(url,
                        request.getUrlEncodedParams(),
                        GoogleTokenResponse.class);

        response.validate();

        return JwtTokenProvider.createToken(response.getAccessToken(),response.getRefreshToken());
    }

    @Override
    public AuthId verify(String accessToken) {
        String url = GoogleApi.USER_INFO.getUrl();

        HttpHeaders headers = makeAuthorizationBearerTokenHeader(accessToken);

        GoogleUserInfoResponse response =
                restClient.getRequest(url, headers, GoogleUserInfoResponse.class);

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
