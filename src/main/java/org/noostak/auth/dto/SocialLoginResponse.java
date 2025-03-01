package org.noostak.auth.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class SocialLoginResponse {
    private String accessToken;
    private String refreshToken;
    private Long memberId;
    private String code;
    private String authType;
}
