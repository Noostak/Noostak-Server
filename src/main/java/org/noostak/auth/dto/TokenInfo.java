package org.noostak.auth.dto;


import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class TokenInfo {
    String authId;

    private TokenInfo(String authId){
        this.authId=authId;
    }

    public static TokenInfo of(String authId){
        return new TokenInfo(authId);
    }
}
