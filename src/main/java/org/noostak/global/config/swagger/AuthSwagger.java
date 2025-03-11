package org.noostak.global.config.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.noostak.auth.dto.common.SignInRequest;
import org.noostak.auth.dto.common.SignUpRequest;
import org.noostak.global.success.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "Auth", description = "인증 및 인가 관련 API")
public interface AuthSwagger {

    @Operation(summary = "로그인", description = "소셜 로그인을 진행하고 인증 정보를 조회하는 API")
    ResponseEntity<SuccessResponse> signIn(
            @Parameter(description = "소셜 로그인 액세스 토큰", example = "Bearer token")
            @RequestHeader("Authorization") String authAccessToken,
            @RequestBody SignInRequest requestDto
    );

    @Operation(summary = "회원가입", description = "새로운 멤버로 회원가입하는 API")
    ResponseEntity<SuccessResponse> signUp(
            @Parameter(description = "소셜 로그인 액세스 토큰", example = "Bearer token")
            @RequestHeader("Authorization") String authAccessToken,
            @ModelAttribute SignUpRequest requestDto
    );

    @Operation(summary = "토큰 재발급", description = "액세스 토큰을 재발급하는 API")
    ResponseEntity<SuccessResponse> tokenReissue(
            @Parameter(description = "리프레시 토큰", example = "Bearer refreshToken")
            @RequestHeader("Authorization") String givenRefreshToken
    );

    @Operation(summary = "회원 탈퇴", description = "멤버 탈퇴 및 소셜 인증 정보를 삭제하는 API")
    ResponseEntity<SuccessResponse> unlink(
            @Parameter(description = "액세스 토큰", example = "Bearer accessToken")
            @RequestHeader("Authorization") String givenAccessToken,
            @Parameter(description = "멤버 ID", example = "1")
            Long memberId
    );

    @Operation(summary = "로그아웃", description = "로그아웃 API")
    ResponseEntity<SuccessResponse> logout(
            @Parameter(description = "액세스 토큰", example = "Bearer accessToken")
            @RequestHeader("Authorization") String givenAccessToken
    );
}
