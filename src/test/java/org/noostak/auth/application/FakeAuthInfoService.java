package org.noostak.auth.application;

import org.noostak.auth.application.jwt.JwtToken;
import org.noostak.auth.common.exception.AuthErrorCode;
import org.noostak.auth.common.exception.AuthException;
import org.noostak.auth.domain.AuthInfo;
import org.noostak.auth.domain.FakeAuthInfoRepository;
import org.noostak.auth.domain.vo.AuthId;
import org.noostak.auth.domain.vo.AuthType;
import org.noostak.auth.domain.vo.RefreshToken;
import org.noostak.auth.dto.SignUpResponse;
import org.noostak.auth.dto.common.SignInResponse;
import org.noostak.auth.dto.common.TokenResponse;
import org.noostak.member.domain.Member;

public class FakeAuthInfoService implements AuthInfoService {

    private final FakeAuthInfoRepository authInfoRepository = new FakeAuthInfoRepository();

    @Override
    public SignUpResponse createAuthInfo(String authType, AuthId authId, Member member) {
        if (authInfoRepository.hasAuthInfoByAuthId(authId)) {
            throw new AuthException(AuthErrorCode.AUTHID_ALREADY_EXISTS,authId.value());
        }

        JwtToken jwtToken = JwtToken.of("accessToken","refreshToken");

        AuthInfo newAuthInfo = AuthInfo.of(
                AuthType.from(authType),
                authId,
                RefreshToken.from(jwtToken.getRefreshToken()),
                member
        );

        authInfoRepository.save(newAuthInfo);

        return SignUpResponse.of(
                jwtToken.getAccessToken(),
                jwtToken.getRefreshToken(),
                member.getId(),
                authType
        );
    }

    @Override
    public void deleteAuthInfo(AuthInfo authInfo) {

    }

    @Override
    public SignInResponse fetchByAuthId(AuthId authId) {
        AuthInfo authInfo = authInfoRepository.getAuthInfoByAuthId(authId);

        return SignInResponse.of(
                "AccessToken",
                authInfo.getRefreshToken().value(),
                authInfo.getMember().getId(),
                authInfo.getAuthType().getName());
    }

    @Override
    public AuthInfo verify(String accessToken) {
        return null;
    }

    @Override
    public AuthInfo findByAuthId(AuthId authId) {
        return authInfoRepository.getAuthInfoByAuthId(authId);
    }

    @Override
    public TokenResponse reIssueAccessToken(String refreshToken) {
        return null;
    }

    @Override
    public JwtToken createToken(AuthId authId) {
        return null;
    }

    @Override
    public AuthInfo updateRefreshToken(AuthId authId, String refreshToken) {
        AuthInfo authInfo = authInfoRepository.getAuthInfoByAuthId(authId);
        authInfo.setRefreshToken(RefreshToken.from(refreshToken));
        return authInfoRepository.save(authInfo);
    }

    @Override
    public boolean hasAuthInfo(AuthId authId) {
        return authInfoRepository.hasAuthInfoByAuthId(authId);
    }

}
