package org.noostak.auth.domain.vo;


import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import org.noostak.auth.common.exception.AuthErrorCode;
import org.noostak.auth.common.exception.AuthException;

@Embeddable
@EqualsAndHashCode
public class RefreshToken {

    private final String token;

    protected RefreshToken() {
        this.token = null;
    }

    private RefreshToken(String token) {
        validate(token);
        this.token = token;
    }

    public static RefreshToken from(String token){
        return new RefreshToken(token);
    }

    private void validate(String token){
        if(token == null){
            throw new AuthException(AuthErrorCode.REFRESH_TOKEN_IS_NOT_NULL);
        }
    }

    public String value(){
        return token;
    }
}
