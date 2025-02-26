package org.noostak.auth.dto;

import lombok.Getter;

@Getter
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
    private String authType;

    private TokenResponse(String accessToken, String refreshToken, String authType) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.authType = authType;
    }

    public static TokenResponse of(String accessToken, String refreshToken, String authType) {
        return new TokenResponse(accessToken,refreshToken,authType);
    }
}
