package org.noostak.auth.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class SocialLoginRequest {
    private String authType;
}
