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
public class AuthController {

    private final OauthServiceFactory oauthServiceFactory;
    private final AuthInfoService authInfoService;
    private final MemberService memberService;

    @PostMapping("/sign-in")
    public ResponseEntity<SuccessResponse> signIn(
            @RequestHeader("Authorization") String authAccessToken
            , @RequestBody SignInRequest requestDto) {

        String givenAuthAccessToken = JwtToken.extractToken(authAccessToken);

        String authType = requestDto.getAuthType();
        OauthService oauthService = oauthServiceFactory.getService(authType);

        AuthId authId = oauthService.verifyByAuthAccessToken(givenAuthAccessToken);

        SignInResponse response = authInfoService.fetchByAuthId(authId);

        return ResponseEntity.ok((SuccessResponse.of(AuthSuccessCode.SIGN_IN_COMPLETED, response)));
    }


    @PostMapping("/sign-up")
    public ResponseEntity<SuccessResponse> signUp(
            @RequestHeader("Authorization") String authAccessToken,
            @ModelAttribute SignUpRequest requestDto) {
        String givenAuthAccessToken = JwtToken.extractToken(authAccessToken);

        String authType = requestDto.getAuthType();
        OauthService oauthService = oauthServiceFactory.getService(authType);

        AuthId verifiedAuthId = oauthService.verifyByAuthAccessToken(givenAuthAccessToken);

        if (authInfoService.hasAuthInfo(verifiedAuthId)) {
            throw new AuthException(AuthErrorCode.AUTHID_ALREADY_EXISTS, verifiedAuthId.value());
        }

        Member member = memberService.createMember(requestDto);

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
            @RequestHeader("Authorization") String givenAccessToken
    ) {
        String accessToken = JwtToken.extractToken(givenAccessToken);
        AuthInfo authInfo = authInfoService.verify(accessToken);

        authInfoService.deleteAuthInfo(authInfo);

        return ResponseEntity.ok((SuccessResponse.of(AuthSuccessCode.UNLINK_COMPLETED)));
    }


    @PostMapping("/logout")
    public ResponseEntity<SuccessResponse> logout(@RequestHeader("Authorization") String givenAccessToken) {
        String accessToken = JwtToken.extractToken(givenAccessToken);

        // TODO: 액세스 토큰 강제 만료 시키기
        return ResponseEntity.ok((SuccessResponse.of(AuthSuccessCode.LOGOUT_COMPLETED)));
    }
}
