package org.noostak.auth.application;

import org.noostak.auth.application.jwt.JwtToken;
import org.noostak.auth.common.exception.AuthException;
import org.noostak.auth.domain.AuthInfo;
import org.noostak.auth.domain.vo.AuthId;
import org.noostak.auth.dto.SignUpResponse;
import org.noostak.auth.dto.common.SignInResponse;
import org.noostak.auth.dto.common.TokenResponse;
import org.noostak.member.domain.Member;

public interface AuthInfoService {
    SignUpResponse createAuthInfo(String authType, AuthId authId, Member member);

    void deleteAuthInfo(AuthInfo authInfo);

    SignInResponse fetchByAuthId(AuthId authId) throws AuthException;

    AuthInfo verify(String accessToken);

    AuthInfo findByAuthId(AuthId authId);

    TokenResponse reIssueAccessToken(String refreshToken);

    JwtToken createToken(AuthId authId);

    AuthInfo updateRefreshToken(AuthId authId, String refreshToken);

    boolean hasAuthInfo(AuthId authId);
}
