package org.noostak.auth.application;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AppleApi {
    TOKEN_REQUEST("https://appleid.apple.com/auth/token"),
    // USER_INFO는 토큰을 디코딩하는 형식
    // LOGOUT은 클라이언트가 토큰을 삭제하는 형식
    ;

    private final String url;
}
