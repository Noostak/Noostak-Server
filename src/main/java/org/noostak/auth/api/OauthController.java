package org.noostak.auth.api;


import lombok.RequiredArgsConstructor;
import org.noostak.auth.application.OauthService;
import org.noostak.auth.application.OauthServiceFactory;
import org.noostak.auth.application.jwt.JwtToken;
import org.noostak.auth.common.exception.AuthErrorCode;
import org.noostak.auth.common.exception.AuthException;
import org.noostak.auth.common.success.AuthSuccessCode;
import org.noostak.auth.domain.AuthInfo;
import org.noostak.auth.domain.vo.AuthId;
import org.noostak.auth.dto.SignUpResponse;
import org.noostak.auth.dto.common.*;
import org.noostak.global.success.SuccessResponse;
import org.noostak.auth.application.AuthInfoService;
import org.noostak.member.application.MemberService;
import org.noostak.member.domain.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class OauthController {

    private final OauthServiceFactory oauthServiceFactory;
    private final AuthInfoService authInfoService;
    private final MemberService memberService;

    @PostMapping("/sign-in")
    public ResponseEntity<SuccessResponse> signIn(
            @RequestHeader("Authorization") String authAccessToken
            , @RequestBody SignInRequest requestDto) {

        // 외부 Provider가 제공한 AuthAccessToken을 추출
        String givenAuthAccessToken = JwtToken.extractToken(authAccessToken);

        // OauthService 선택 (Kakao, Google, Apple)
        String authType = requestDto.getAuthType();
        OauthService oauthService = oauthServiceFactory.getService(authType);

        // 소셜 로그인을 통해 유저 정보 불러오기, 만약 유효하지 않은 액세스 토큰일 경우 내부에서 에러 발생
        AuthId authId = oauthService.verifyByAuthAccessToken(givenAuthAccessToken);

        // 조회 결과 반환, authId를 통해 저장된 refreshToken과 accessToken을 갱신
        SignInResponse response = authInfoService.fetchByAuthId(authId);

        return ResponseEntity.ok((SuccessResponse.of(AuthSuccessCode.SIGN_IN_COMPLETED, response)));
    }


    @PostMapping("/sign-up")
    public ResponseEntity<SuccessResponse> signUp(
            @RequestHeader("Authorization") String authAccessToken,
            @ModelAttribute SignUpRequest requestDto) {
        String givenAuthAccessToken = JwtToken.extractToken(authAccessToken);

        // OauthService 선택하기 (Kakao, Google, Apple)
        String authType = requestDto.getAuthType();
        OauthService oauthService = oauthServiceFactory.getService(authType);

        // 소셜 서비스 로그인 진행하기(유저 정보 불러오기)
        AuthId verifiedAuthId = oauthService.verifyByAuthAccessToken(givenAuthAccessToken);

        // 동일 소셜 계정으로 가입이 되어있는지 확인하기
        if (authInfoService.hasAuthInfo(verifiedAuthId)) {
            throw new AuthException(AuthErrorCode.AUTHID_ALREADY_EXISTS, verifiedAuthId.value());
        }

        // 멤버 생성하기
        Member member = memberService.createMember(requestDto);

        // 멤버와 연관된 AuthInfo 생성하기
        SignUpResponse response =
                authInfoService.createAuthInfo(authType, verifiedAuthId, member);

        return ResponseEntity.ok((SuccessResponse.of(AuthSuccessCode.SIGN_UP_COMPLETED, response)));
    }

    @PostMapping("/token-reissue")
    public ResponseEntity<SuccessResponse> tokenReissue(@RequestHeader("Authorization") String givenRefreshToken) {
        String refreshToken = JwtToken.extractToken(givenRefreshToken);
        TokenResponse response = authInfoService.reIssueAccessToken(refreshToken);

        return ResponseEntity.ok((SuccessResponse.of(AuthSuccessCode.TOKEN_REISSUE_COMPLETED, response)));
    }

    @DeleteMapping("/withdraw")
    public ResponseEntity<SuccessResponse> unlink(
            @RequestHeader("Authorization") String givenAccessToken,
            @RequestAttribute Long memberId
    ) {
        String accessToken = JwtToken.extractToken(givenAccessToken);
        AuthInfo authInfo = authInfoService.verify(accessToken);

        // 멤버 및 소셜 정보 삭제
        authInfoService.deleteAuthInfo(authInfo);
        memberService.deleteMember(memberId);

        return ResponseEntity.ok((SuccessResponse.of(AuthSuccessCode.UNLINK_COMPLETED)));
    }


    @PostMapping("/logout")
    public ResponseEntity<SuccessResponse> logout(@RequestHeader("Authorization") String givenAccessToken) {
        String accessToken = JwtToken.extractToken(givenAccessToken);

        // TODO: 액세스 토큰 강제 만료 시키기

        return ResponseEntity.ok((SuccessResponse.of(AuthSuccessCode.LOGOUT_COMPLETED)));
    }
}
