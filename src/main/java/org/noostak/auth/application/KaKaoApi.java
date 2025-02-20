package org.noostak.auth.application;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum KaKaoApi {
    TOKEN_REQUEST("https://kauth.kakao.com/oauth/token"),
    FETCH_TOKEN("https://kapi.kakao.com/v1/user/access_token_info"),
    FETCH_MY_INFO("https://kapi.kakao.com/v2/user/me")
    ;

    private final String url;
}
