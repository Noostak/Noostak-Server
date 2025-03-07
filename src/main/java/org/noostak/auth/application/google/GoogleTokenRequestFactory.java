package org.noostak.auth.application.google;

import org.noostak.auth.dto.google.GoogleAccessTokenRequest;
import org.noostak.auth.dto.google.GoogleTokenRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class GoogleTokenRequestFactory {


    private final String clientId;


    private final String redirectUri;


    private final String clientSecret;

    public GoogleTokenRequestFactory(
            @Value("${oauth-property.google.client_id}") String clientId,
            @Value("${oauth-property.google.redirect_uri}") String redirectUri,
            @Value("${oauth-property.google.client_secret}") String clientSecret) {
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.clientSecret = clientSecret;
    }

    public GoogleTokenRequest createRequest(String code) {
        return GoogleTokenRequest.of(clientId, redirectUri, code, clientSecret);
    }

    public GoogleAccessTokenRequest createAccessTokenRequest(String refreshToken) {
        return GoogleAccessTokenRequest.of(clientId, refreshToken, clientSecret);
    }
}