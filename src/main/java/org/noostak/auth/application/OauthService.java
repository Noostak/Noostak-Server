package org.noostak.auth.application;

import org.noostak.auth.application.jwt.JwtToken;
import org.noostak.auth.domain.vo.AuthId;
import org.noostak.auth.dto.TokenInfo;

public interface OauthService {

    void requestAccessToken(String refreshToken); // 액세스 토큰 재발급

    TokenInfo fetchTokenInfo(String accessToken); // 토큰 정보 가져오기

    JwtToken requestToken(String code); // 토큰 정보 발급

    AuthId login(String accessToken); // 로그인 처리
}
