package org.noostak.auth.dto.common;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class SignInRequest {
    String authType;

    public String getAuthType(){
        return authType.toUpperCase();
    }
}
