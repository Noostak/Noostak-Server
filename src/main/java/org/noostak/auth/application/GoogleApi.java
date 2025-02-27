package org.noostak.auth.application;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GoogleApi {
    TOKEN_REQUEST("https://oauth2.googleapis.com/token"),
    USER_INFO("https://www.googleapis.com/oauth2/v2/userinfo"),
    LOGOUT("https://kauth.kakao.com/oauth/logout"),
    UNLINK("https://oauth2.googleapis.com/revoke")

    ;

    private final String url;
}
