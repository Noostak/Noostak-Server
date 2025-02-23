package org.noostak.auth.application.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.StringUtils;

@Getter
@ToString
@AllArgsConstructor
public class JwtToken {
    private String tokenType = "Bearer";
    private String accessToken;
    private String refreshToken;


    public static String extractToken(String bearerToken) {
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public void setRefreshToken(String refreshToken){
        this.refreshToken=refreshToken;
    }
    public boolean refreshTokenIsExists(){return this.refreshToken != null;}
}
