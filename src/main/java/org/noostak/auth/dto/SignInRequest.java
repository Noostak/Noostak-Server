package org.noostak.auth.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignInRequest {
    String authType;

    public String getAuthType(){
        return authType.toUpperCase();
    }
}
