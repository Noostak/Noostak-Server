package org.noostak.auth.application;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class GoogleServiceImpl implements GoogleService{

    private final GoogleTokenRequestFactory googleTokenRequestFactory;
    private final RestClient restClient;

    @Override
    public void requestAccessToken(String refreshToken) {

    }


    @Override
    public TokenInfo fetchTokenInfo(String accessToken) {
        return null;
    }


    @Override
    public JwtToken requestToken(String code) {
        String url = GoogleApi.TOKEN_REQUEST.getUrl();

        GoogleTokenRequest request = googleTokenRequestFactory.createRequest(code);

        GoogleTokenResponse response =
                restClient.postRequest(url,
                        request.getUrlEncodedParams(),
                        GoogleTokenResponse.class);

        log.info("googleTokenResponse: {}",response);
        response.validate();

        return JwtTokenProvider.createToken(response.getAccessToken(),response.getRefreshToken());
    }

    @Override
    public AuthId login(String accessToken) {
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
