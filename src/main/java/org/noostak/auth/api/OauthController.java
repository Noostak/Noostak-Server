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

        // TODO: 액세스 토큰 정보 가져오기(카카오 403 에러로 인한 보류)
        //  TokenInfo tokenInfo = oauthService.fetchTokenInfo(accessToken);

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

//    @PostMapping(path = "/sign-in")
//    public ResponseEntity<SuccessResponse<?>> signIn(
//            HttpServletRequest request,
//          @RequestBody SocialLoginRequest requestDto
//    ) {
//
//        // TODO: 멤버의 회원가입 여부 파악하기 (해당 authId를 가진 멤버가 존재하는가?)
//        String givenAuthId = request.getHeader("Authorization");
//        AuthType authType = AuthType.from(requestDto.getAuthType());
//
//        // TODO: 만약 authId에 대한 정보가 없다면 응답 반환하기
//        if(!authInfoService.hasAuthInfo(givenAuthId)){
//
//            return ResponseEntity.ok((SuccessResponse.of(AuthErrorCode.AUTH_ID_ALREADY_EXISTS,givenAuthId)));
//        }
//
//        // TODO: 2. 만약 정보가 이미 있다면 authId로 카카오 서버에 토큰 요청하기
//        JwtToken jwtToken = kakaoService.requestToken(requestDto.getAuthId());
//
//        // TODO: 이미 존재하는 멤버 AuthInfo의 RefreshToken 갱신하기
//
//        // TODO: JWT 토큰 반환하기
//        return ResponseEntity.ok((SuccessResponse.of(GROUP_CREATED, response)));
//    }
}
