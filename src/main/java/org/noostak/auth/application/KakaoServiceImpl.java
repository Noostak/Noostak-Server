package org.noostak.auth.application;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noostak.auth.application.jwt.JwtToken;
import org.noostak.auth.application.jwt.JwtTokenProvider;
import org.noostak.auth.common.exception.AuthErrorCode;
import org.noostak.auth.common.exception.AuthException;
import org.noostak.auth.domain.vo.AuthId;
import org.noostak.auth.dto.*;
import org.noostak.global.KakaoTokenRequestFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KakaoServiceImpl implements KakaoService{

    private final KakaoTokenRequestFactory kakaoTokenRequestFactory;
    private final RestClient restClient;
    @Override
    public void requestAccessToken(String refreshToken) {

    }


    @Override
    public TokenInfo fetchTokenInfo(String accessToken) {
        String url = KaKaoApi.FETCH_TOKEN.getUrl();
        HttpHeaders headers = makeAuthorizationBearerTokenHeader(accessToken);

        KakaoTokenInfoResponse response =
                restClient.postRequest(url, headers, KakaoTokenInfoResponse.class);

        response.validate();

        return TokenInfo.of(response.getId());
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
    public AuthId login(String accessToken) {
        String url = KaKaoApi.FETCH_MY_INFO.getUrl();

        HttpHeaders headers = makeAuthorizationBearerTokenHeader(accessToken);

        KakaoMyInfoResponse response =
                restClient.postRequest(url, headers, KakaoMyInfoResponse.class);

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

    // TODO: 연결 끊기(멤버 delete, authInfo delete)
    // TODO: 로그아웃(멤버 로그아웃)
}
