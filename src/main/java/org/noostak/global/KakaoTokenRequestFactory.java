package org.noostak.global;

import org.noostak.auth.dto.KakaoTokenRequest;

public class KakaoTokenRequestFactory {
    private final String clientId;
    private final String redirectUri;

    public KakaoTokenRequestFactory(String clientId, String redirectUri) {
        this.clientId = clientId;
        this.redirectUri = redirectUri;
    }

    public KakaoTokenRequest createRequest(String code) {
        return KakaoTokenRequest.of(clientId, redirectUri, code);
    }
}