package org.noostak.auth.application;

import org.noostak.auth.dto.apple.AppleAccessTokenRequest;
import org.noostak.auth.dto.apple.AppleTokenRequest;
import org.noostak.auth.dto.kakao.KakaoAccessTokenRequest;
import org.noostak.auth.dto.kakao.KakaoLogoutRequest;
import org.noostak.auth.dto.kakao.KakaoTokenRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class AppleTokenRequestFactory {

    @Value("${oauth-property.apple.client_id}")
    private final String clientId;

    @Value("${oauth-property.apple.client_secret}")
    private final String clientSecret;


    public AppleTokenRequestFactory(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public AppleTokenRequest createRequest(String code) {
        return AppleTokenRequest.of(clientId, code,clientSecret);
    }

    public AppleAccessTokenRequest createAccessTokenRequest(String refreshToken) {
        return AppleAccessTokenRequest.of(clientId, refreshToken, clientSecret);
    }
}