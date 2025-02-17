package org.noostak.auth.application;

import org.noostak.auth.application.jwt.JwtToken;
import org.noostak.auth.dto.SignUpResponse;
import org.noostak.member.domain.Member;

public interface AuthInfoService {
    SignUpResponse createAuthInfo(String authType, String authId, JwtToken jwtToken, Member member);

    boolean hasAuthInfo(String authId);
}
