package org.noostak.auth.dto.common;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class TokenResponse {
    private final String accessToken;
    private final String refreshToken;
    private final String authId;

    private TokenResponse(String accessToken, String refreshToken, String authId) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.authId = authId;
    }

    public static TokenResponse of(String accessToken, String refreshToken, String authId) {
        return new TokenResponse(accessToken,refreshToken,authId);
    }
}
