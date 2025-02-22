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

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(HttpServletRequest request, @ModelAttribute SignUpRequest requestDto){
        String code = request.getHeader("Authorization");

        // authType 을 기준으로 OauthService 선택하기
        String authType = requestDto.getAuthType().toUpperCase();
        OauthService oauthService = oauthServiceFactory.getService(authType);

        // code를 통해서 AccessToken 및 RefreshToken 가져오기
        JwtToken jwtToken = oauthService.requestToken(code);
        String accessToken = jwtToken.getAccessToken();
        log.info("jwtToken : {}", jwtToken);

        // 소셜 로그인 진행하기
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

        return ResponseEntity.ok((SuccessResponse.of(AuthSuccessCode.SIGNUP_COMPLETED,response)));
    }
}
