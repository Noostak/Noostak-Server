package org.noostak.auth.dto.common;


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
