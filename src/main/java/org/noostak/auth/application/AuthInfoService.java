package org.noostak.auth.application;

import org.noostak.auth.application.jwt.JwtToken;
import org.noostak.auth.domain.AuthInfo;
import org.noostak.auth.domain.vo.AuthId;
import org.noostak.auth.dto.SignUpResponse;
import org.noostak.auth.dto.common.SignInResponse;
import org.noostak.member.domain.Member;

public interface AuthInfoService {
    SignUpResponse createAuthInfo(String authType, AuthId authId, JwtToken jwtToken, Member member);

    SignInResponse fetchByAuthId(AuthId authId, String accessToken);

    AuthInfo findByAuthId(AuthId authId);

    AuthInfo updateRefreshToken(AuthId authId, String refreshToken);

    boolean hasAuthInfo(AuthId authId);
}
