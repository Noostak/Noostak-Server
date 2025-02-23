package org.noostak.auth.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthorizeRequest {
    String authType;
    String code;

    public String getAuthType(){
        return authType.toUpperCase();
    }
}
