package org.noostak.auth.application;

import org.noostak.auth.application.jwt.JwtToken;
import org.noostak.auth.common.exception.ExternalApiException;
import org.noostak.auth.domain.vo.AuthId;

public interface OauthService {

    JwtToken requestAccessToken(String refreshToken) throws ExternalApiException; // 액세스 토큰 재발급

    JwtToken requestToken(String code); // 토큰 정보 발급

    AuthId verify(String accessToken); // 로그인 처리

}
