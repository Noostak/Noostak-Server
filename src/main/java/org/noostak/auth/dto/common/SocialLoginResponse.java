package org.noostak.auth.dto.common;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SocialLoginResponse {
    private String accessToken;
    private String refreshToken;
    private Long memberId;
    private String code;
    private String authType;
}
