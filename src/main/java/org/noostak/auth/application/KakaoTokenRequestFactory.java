package org.noostak.auth.application;

import org.noostak.auth.dto.KakaoAccessTokenRequest;
import org.noostak.auth.dto.KakaoTokenRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class KakaoTokenRequestFactory {

    @Value("${oauth-property.kakao.client_id}")
    private final String clientId;

    @Value("${oauth-property.kakao.redirect_uri}")
    private final String redirectUri;

    @Value("${oauth-property.kakao.client_secret}")
    private final String clientSecret;


    public KakaoTokenRequestFactory(String clientId, String redirectUri, String clientSecret) {
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.clientSecret = clientSecret;
    }

    public KakaoTokenRequest createRequest(String code) {
        return KakaoTokenRequest.of(clientId, redirectUri, code,clientSecret);
    }

    public KakaoAccessTokenRequest createAccessTokenRequest(String refreshToken) {
        return KakaoAccessTokenRequest.of(clientId, refreshToken, clientSecret);
    }
}