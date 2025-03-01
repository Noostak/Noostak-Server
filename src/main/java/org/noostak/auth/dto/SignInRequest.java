package org.noostak.auth.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class SignInRequest {
    String authType;

    public String getAuthType(){
        return authType.toUpperCase();
    }
}
