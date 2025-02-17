package org.noostak.auth.domain.vo;


import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class RefreshToken {

    private final String token;

    protected RefreshToken() {
        this.token = null;
    }

    private RefreshToken(String token) {
        this.token = token;
    }

    public static RefreshToken from(String token){
        return new RefreshToken(token);
    }
    public boolean expired(){
        return true;
    }
}
