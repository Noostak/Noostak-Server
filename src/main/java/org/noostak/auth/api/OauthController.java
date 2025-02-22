package org.noostak.auth.api;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noostak.auth.application.OauthService;
import org.noostak.auth.application.OauthServiceFactory;
import org.noostak.auth.application.jwt.JwtToken;
import org.noostak.auth.common.exception.AuthErrorCode;
import org.noostak.auth.common.exception.AuthException;
import org.noostak.auth.common.success.AuthSuccessCode;
import org.noostak.auth.domain.vo.AuthId;
import org.noostak.auth.dto.SignInRequest;
import org.noostak.auth.dto.SignInResponse;
import org.noostak.auth.dto.SignUpResponse;
import org.noostak.global.success.SuccessResponse;
import org.noostak.auth.application.AuthInfoService;
import org.noostak.auth.dto.SignUpRequest;
import org.noostak.member.application.MemberService;
import org.noostak.member.domain.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/auth")
public class OauthController {

    private final OauthServiceFactory oauthServiceFactory;
    private final AuthInfoService authInfoService;
    private final MemberService memberService;

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(HttpServletRequest request, @RequestBody SignInRequest requestDto){
        String givenAccessToken = request.getHeader("Authorization");

        String authType = requestDto.getAuthType();
        OauthService oauthService = oauthServiceFactory.getService(authType);

        // TODO: 소셜 서비스 로그인 진행하기(유저 정보 불러오기)
        //  -> 만약 유효하지 않은 액세스 토큰일 경우, 내부에서 에러 발생
        AuthId authId = oauthService.login(givenAccessToken);

        SignInResponse response = authInfoService.fetchByAuthId(authId,givenAccessToken);

        // TODO: refreshToken으로 response 갱신하기
        String refreshToken = response.getRefreshToken();

        return ResponseEntity.ok((SuccessResponse.of(AuthSuccessCode.SIGN_IN_COMPLETED,response)));
    }


    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(HttpServletRequest request, @ModelAttribute SignUpRequest requestDto){
        String code = request.getHeader("Authorization");

        // authType 을 기준으로 OauthService 선택하기
        String authType = requestDto.getAuthType();
        OauthService oauthService = oauthServiceFactory.getService(authType);

        // code를 통해서 AccessToken 및 RefreshToken 가져오기
        JwtToken jwtToken = oauthService.requestToken(code);
        String accessToken = jwtToken.getAccessToken();

        // 소셜 서비스 로그인 진행하기(유저 정보 불러오기)
        AuthId authId = oauthService.login(accessToken);

        // 동일 소셜 계정으로 가입이 되어있는지 확인하기
        if(authInfoService.hasAuthInfo(authId)){
            throw new AuthException(AuthErrorCode.AUTHID_ALREADY_EXISTS,authId.value());
        }

        // 멤버 생성하기
        Member member = memberService.createMember(requestDto);

        // 멤버와 연관된 AuthInfo 생성하기
        SignUpResponse response =
                authInfoService.createAuthInfo(authType, authId, jwtToken, member);

        return ResponseEntity.ok((SuccessResponse.of(AuthSuccessCode.SIGN_UP_COMPLETED,response)));
    }
}
