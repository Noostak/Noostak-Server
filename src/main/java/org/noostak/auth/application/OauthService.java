package org.noostak.auth.application;

import org.noostak.auth.domain.vo.AuthId;

public interface OauthService {


    AuthId verifyByAuthAccessToken(String accessToken); // 로그인 처리

    void logout(String accessToken); // 로그 아웃

    void unlink(String accessToken); // 연결 끊기
}
