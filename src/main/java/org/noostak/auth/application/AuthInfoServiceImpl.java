package org.noostak.auth.application;

import lombok.RequiredArgsConstructor;
import org.noostak.auth.application.jwt.JwtToken;
import org.noostak.auth.domain.AuthInfo;
import org.noostak.auth.domain.AuthInfoRepository;
import org.noostak.auth.domain.vo.AuthId;
import org.noostak.auth.domain.vo.AuthType;
import org.noostak.auth.domain.vo.RefreshToken;
import org.noostak.auth.dto.SignUpResponse;
import org.noostak.member.domain.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class AuthInfoServiceImpl implements AuthInfoService{

    private final AuthInfoRepository authInfoRepository;
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

    private AuthInfo createAuthInfo(AuthType authType, AuthId authId, RefreshToken refreshToken, Member member) {
        return AuthInfo.of(
                authType,
                authId,
                refreshToken,
                member
        );
    }

    private AuthInfo saveAuthInfo(AuthInfo authInfo){
        return authInfoRepository.save(authInfo);
    }

    @Override
    public boolean hasAuthInfo(String authId){
        return authInfoRepository.hasAuthInfoByAuthId(AuthId.from(authId));
    }

    @Override
    public boolean hasAuthInfo(AuthId authId) {
        return authInfoRepository.hasAuthInfoByAuthId(authId);
    }
}
