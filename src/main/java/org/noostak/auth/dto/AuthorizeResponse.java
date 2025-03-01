package org.noostak.auth.dto;


import lombok.Getter;
import lombok.ToString;
import org.noostak.auth.application.jwt.JwtToken;
import org.noostak.auth.domain.vo.AuthId;

@Getter
@ToString
public class AuthorizeResponse {

    private boolean isMember;
    private String accessToken;
    private String refreshToken;
    private String authId;
    private String authType;

    private AuthorizeResponse(boolean isMember, String accessToken,
                              String refreshToken, String authId, String authType) {
        this.isMember = isMember;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.authId = authId;
        this.authType = authType;
    }

    public static AuthorizeResponse of(boolean isMember, AuthId authId, String authType, JwtToken jwtToken ) {
        return new AuthorizeResponse(isMember, jwtToken.getAccessToken(),
                jwtToken.getRefreshToken(), authId.value(), authType);
    }
}
