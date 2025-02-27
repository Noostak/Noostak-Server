package org.noostak.auth.dto;


import lombok.Getter;

@Getter
public class SignInResponse {
    private String accessToken;
    private String refreshToken;
    private Long memberId;
    private String authType;

    private SignInResponse(String accessToken, String refreshToken, Long memberId, String authType) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.memberId = memberId;
        this.authType = authType;
    }

    public static SignInResponse of(String accessToken, String refreshToken, Long memberId, String authType){
        return new SignInResponse(accessToken,refreshToken,memberId,authType);
    }

    public void setAccessToken(String accessToken){
        this.accessToken = accessToken;
    }
}
