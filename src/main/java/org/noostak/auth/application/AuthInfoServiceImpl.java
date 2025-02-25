package org.noostak.auth.application;

import lombok.RequiredArgsConstructor;
import org.noostak.auth.application.jwt.JwtToken;
import org.noostak.auth.domain.AuthInfo;
import org.noostak.auth.domain.AuthInfoRepository;
import org.noostak.auth.domain.vo.AuthId;
import org.noostak.auth.domain.vo.AuthType;
import org.noostak.auth.domain.vo.RefreshToken;
import org.noostak.auth.dto.AuthorizeResponse;
import org.noostak.auth.dto.SignInResponse;
import org.noostak.auth.dto.SignUpResponse;
import org.noostak.member.domain.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthInfoServiceImpl implements AuthInfoService {

    private final AuthInfoRepository authInfoRepository;
    private final AuthTokenResolver authTokenResolver;

    @Override
    @Transactional
    public SignUpResponse createAuthInfo(String authType, AuthId authId, JwtToken jwtToken, Member member) {
        AuthInfo newAuthInfo = createAuthInfo(
                AuthType.from(authType),
                authId,
                RefreshToken.from(jwtToken.getRefreshToken()),
                member
        );

        saveAuthInfo(newAuthInfo);

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
    @Transactional
    public AuthInfo updateRefreshToken(AuthId authId, String refreshToken) {
        AuthInfo authInfo = authInfoRepository.getAuthInfoByAuthId(authId);
        authInfo.setRefreshToken(RefreshToken.from(refreshToken));

        return authInfoRepository.save(authInfo);
    }

    private AuthInfo createAuthInfo(AuthType authType, AuthId authId, RefreshToken refreshToken, Member member) {
        return AuthInfo.of(
                authType,
                authId,
                refreshToken,
                member
        );
    }

    @Override
    public AuthorizeResponse authorize(String authType, AuthId authId, JwtToken jwtToken) {
        boolean isMember = hasAuthInfo(authId);

        if (isMember) {
            updateRefreshToken(authId, jwtToken.getRefreshToken());
        }else{
            // 멤버가 아닐 경우 인증 정보를 일시적으로 유지
            authTokenResolver.put(authId, jwtToken);
        }

        return AuthorizeResponse.of(isMember, authId, authType, jwtToken);
    }

    public JwtToken findTempSavedTokenByAuthId(String authId){
        return authTokenResolver.get(AuthId.from(authId));
    }

    private AuthInfo saveAuthInfo(AuthInfo authInfo) {
        return authInfoRepository.save(authInfo);
    }

    @Override
    public boolean hasAuthInfo(AuthId authId) {
        return authInfoRepository.hasAuthInfoByAuthId(authId);
    }
}
