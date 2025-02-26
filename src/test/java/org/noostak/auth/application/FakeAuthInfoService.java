package org.noostak.auth.application;

import org.noostak.auth.application.jwt.JwtToken;
import org.noostak.auth.common.exception.AuthErrorCode;
import org.noostak.auth.common.exception.AuthException;
import org.noostak.auth.domain.AuthInfo;
import org.noostak.auth.domain.FakeAuthInfoRepository;
import org.noostak.auth.domain.vo.AuthId;
import org.noostak.auth.domain.vo.AuthType;
import org.noostak.auth.domain.vo.RefreshToken;
import org.noostak.auth.dto.AuthorizeResponse;
import org.noostak.auth.dto.SignInResponse;
import org.noostak.auth.dto.SignUpResponse;
import org.noostak.member.domain.Member;

public class FakeAuthInfoService implements AuthInfoService {

    private final FakeAuthInfoRepository authInfoRepository = new FakeAuthInfoRepository();

    @Override
    public SignUpResponse createAuthInfo(String authType, AuthId authId, JwtToken jwtToken, Member member) {
        if (authInfoRepository.hasAuthInfoByAuthId(authId)) {
            throw new AuthException(AuthErrorCode.AUTHID_ALREADY_EXISTS,authId.value());
        }

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
    public SignInResponse fetchByAuthId(AuthId authId, String accessToken) {
        AuthInfo authInfo = authInfoRepository.getAuthInfoByAuthId(authId);

        return SignInResponse.of(
                accessToken,
                authInfo.getRefreshToken().value(),
                authInfo.getMember().getId(),
                authInfo.getAuthType().getName());
    }

    @Override
    public AuthInfo findByAuthId(AuthId authId) {
        return authInfoRepository.getAuthInfoByAuthId(authId);
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

    @Override
    public AuthorizeResponse authorize(String authType, AuthId authId, JwtToken jwtToken) {
        boolean isMember = authInfoRepository.hasAuthInfoByAuthId(authId);

        if (isMember) {
            updateRefreshToken(authId, jwtToken.getRefreshToken());
        }

        return AuthorizeResponse.of(isMember, authId, authType, jwtToken);
    }

    @Override
    public JwtToken findTempSavedTokenByAuthId(String authId) {
        return null;
    }
}
