package org.noostak.auth.application.apple;

import org.noostak.auth.dto.apple.AppleAccessTokenRequest;
import org.noostak.auth.dto.apple.AppleTokenRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class AppleTokenRequestFactory {

    private final String clientId;

    private final String clientSecret;

    private final String appleKeyPath;

    private final String teamId;

    private final String keyId;

    private final String aud;

    public AppleTokenRequestFactory
            (@Value("${oauth-property.apple.client_id}") String clientId,
             @Value("${oauth-property.apple.client_secret}") String clientSecret,
             @Value("${oauth-property.apple.key_path}") String appleKeyPath,
             @Value("${oauth-property.apple.team_id}") String teamId,
             @Value("${oauth-property.apple.key_id}") String keyId,
             @Value("${oauth-property.apple.aud}") String aud) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.appleKeyPath = appleKeyPath;
        this.teamId = teamId;
        this.keyId = keyId;
        this.aud = aud;
    }

    public AppleTokenRequest createRequest(String code) {
        return AppleTokenRequest.of(clientId, code,clientSecret);
    }

    public AppleAccessTokenRequest createAccessTokenRequest(String refreshToken) {
        return AppleAccessTokenRequest.of(clientId, refreshToken, clientSecret);
    }
}