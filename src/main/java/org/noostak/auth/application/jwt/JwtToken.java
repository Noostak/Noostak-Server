package org.noostak.auth.application.jwt;

import lombok.Getter;
import lombok.ToString;
import org.noostak.auth.common.exception.AuthErrorCode;
import org.noostak.auth.common.exception.AuthException;
import org.noostak.global.utils.GlobalLogger;
import org.springframework.util.StringUtils;

@Getter
@ToString
public class JwtToken {
    private final String tokenType;
    private final String accessToken;
    private String refreshToken;

    public JwtToken(String accessToken, String refreshToken) {
        this.tokenType = "Bearer";
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static String extractToken(String bearerToken) {
        GlobalLogger.info("Extract: ",bearerToken);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        throw new AuthException(AuthErrorCode.INVALID_TOKEN);
    }

    public static JwtToken of(String accessToken, String refreshToken){
        return new JwtToken(accessToken,refreshToken);
    }

    public void setRefreshToken(String refreshToken){
        this.refreshToken=refreshToken;
    }
    public boolean refreshTokenIsExists(){return this.refreshToken != null;}
}
