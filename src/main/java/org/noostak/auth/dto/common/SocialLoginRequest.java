package org.noostak.auth.dto.common;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class SocialLoginRequest {
    private String authType;
}
