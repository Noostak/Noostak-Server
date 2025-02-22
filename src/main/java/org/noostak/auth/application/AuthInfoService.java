package org.noostak.auth.application;

import org.noostak.auth.application.jwt.JwtToken;
import org.noostak.auth.domain.AuthInfo;
import org.noostak.auth.domain.vo.AuthId;
import org.noostak.auth.dto.SignInResponse;
import org.noostak.auth.dto.SignUpResponse;
import org.noostak.member.domain.Member;

public interface AuthInfoService {
    SignUpResponse createAuthInfo(String authType, AuthId authId, JwtToken jwtToken, Member member);

    SignInResponse fetchByAuthId(AuthId authId);
    SignInResponse fetchByAuthId(AuthId authId, String accessToken);

    boolean hasAuthInfo(String authId);

    boolean hasAuthInfo(AuthId authId);
}
