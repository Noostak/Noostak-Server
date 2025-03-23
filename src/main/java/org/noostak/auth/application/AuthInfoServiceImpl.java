package org.noostak.auth.application;

import lombok.RequiredArgsConstructor;
import org.noostak.appointmentmember.domain.AppointmentMember;
import org.noostak.appointmentmember.domain.AppointmentMemberAvailableTimesRepository;
import org.noostak.appointmentmember.domain.AppointmentMemberRepository;
import org.noostak.auth.application.jwt.JwtToken;
import org.noostak.auth.application.jwt.JwtTokenProvider;
import org.noostak.auth.common.exception.AuthErrorCode;
import org.noostak.auth.common.exception.AuthException;
import org.noostak.auth.domain.AuthInfo;
import org.noostak.auth.domain.AuthInfoRepository;
import org.noostak.auth.domain.vo.AuthId;
import org.noostak.auth.domain.vo.AuthType;
import org.noostak.auth.domain.vo.RefreshToken;
import org.noostak.auth.dto.SignUpResponse;
import org.noostak.auth.dto.common.SignInResponse;
import org.noostak.auth.dto.common.TokenResponse;
import org.noostak.member.application.MemberService;
import org.noostak.member.domain.Member;
import org.noostak.member.domain.MemberRepository;
import org.noostak.membergroup.domain.MemberGroup;
import org.noostak.membergroup.domain.MemberGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthInfoServiceImpl implements AuthInfoService {

    private final AuthInfoRepository authInfoRepository;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberGroupRepository memberGroupRepository;
    private final AppointmentMemberAvailableTimesRepository appointmentMemberAvailableTimesRepository;
    private final AppointmentMemberRepository appointmentMemberRepository;

    @Override
    @Transactional
    public SignUpResponse createAuthInfo(String authType, AuthId authId, Member member) {
        // JWT 토큰 생성하기
        JwtToken jwtToken = createToken(authId);

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
    @Transactional
    public void deleteAuthInfo(AuthInfo authInfo) {
        Member deletedMember = authInfo.getMember();

        // AuthInfo 삭제
        authInfoRepository.delete(authInfo);

        // GroupMember 삭제
        List<MemberGroup> memberGroups = memberGroupRepository.findByMemberId(deletedMember.getId());
        memberGroupRepository.deleteAll(memberGroups);

        // AppointmentMember 삭제
        List<AppointmentMember> appointmentMembers = appointmentMemberRepository.findByMember(deletedMember);

        // AppointmentMemberAvailableTime 삭제
        appointmentMembers.forEach(appointmentMemberAvailableTimesRepository::deleteByAppointmentMember);

        // AppointmentMember 삭제
        appointmentMemberRepository.deleteAll(appointmentMembers);

        // Member 삭제
        memberRepository.delete(deletedMember);
    }

    @Override
    @Transactional
    public SignInResponse fetchByAuthId(AuthId authId) throws AuthException {
        // 주어진 authId에 대한 authInfo가 존재하는지 확인
        AuthInfo authInfo = authInfoRepository.getAuthInfoByAuthId(authId);

        // 액세스, 리프레시 토큰 갱신
        JwtToken token = getJwtToken(authInfo);

        return SignInResponse.of(
                token.getAccessToken(),
                token.getRefreshToken(),
                authInfo.getMember().getId(),
                authInfo.getAuthType().getName());
    }

    @Override
    public AuthInfo verify(String accessToken) {
        // 액세스 토큰을 인증하고 authId 가져오기
        String authId = jwtTokenProvider.getAuthId(accessToken);

        return findByAuthId(AuthId.from(authId));
    }


    @Override
    public TokenResponse reIssueAccessToken(String givenRefreshToken) {
        // 액세스 토큰 새로 생성하여 갱신하기
        AuthInfo authInfo = authInfoRepository.getAuthInfoByRefreshToken(givenRefreshToken);

        String refreshToken = authInfo.getRefreshToken().value();
        String accessToken = jwtTokenProvider.createAccessTokenIfValid(refreshToken);

        // 주어진 액세스 토큰으로 authId 확인
        String authId = jwtTokenProvider.getAuthId(accessToken);

        // authId 유효성 확인
        validateAuthId(authId);

        return TokenResponse.of(accessToken, refreshToken, authId);
    }

    @Override
    public JwtToken createToken(AuthId authId) {
        return jwtTokenProvider.createToken(authId.value());
    }


    private JwtToken getJwtToken(AuthInfo authInfo) {
        AuthId authId = authInfo.getAuthId();

        // 액세스, 리프레시 토큰 갱신
        JwtToken newToken = jwtTokenProvider.createToken(authId.value());

        // 리프레시 토큰 업데이트
        updateRefreshToken(authId, newToken.getRefreshToken());

        return newToken;
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


    private AuthInfo saveAuthInfo(AuthInfo authInfo) {
        return authInfoRepository.save(authInfo);
    }

    private void validateAuthId(String authId){
        // 주어진 authId가 잘못 되었다면 예외 발생
        if (!hasAuthInfo(AuthId.from(authId))) {
            throw new AuthException(AuthErrorCode.AUTH_ID_NOT_EXISTS, authId);
        }
    }

    @Override
    public boolean hasAuthInfo(AuthId authId) {
        return authInfoRepository.hasAuthInfoByAuthId(authId);
    }
}
