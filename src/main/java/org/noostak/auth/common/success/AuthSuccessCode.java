package org.noostak.auth.common.success;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.noostak.global.success.SuccessCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthSuccessCode implements SuccessCode {
    SIGN_UP_COMPLETED(HttpStatus.CREATED, "회원가입이 성공적으로 완료 되었습니다."),
    SIGN_IN_COMPLETED(HttpStatus.OK, "소셜 로그인에 성공했습니다."),
    AUTHORIZE_COMPLETED(HttpStatus.OK, "소셜 사용자 인증을 성공하였습니다."),
    TOKEN_REISSUE_COMPLETED(HttpStatus.OK, "토큰 재발급에 성공하였습니다.")
    ;

    private final HttpStatus status;
    private final String message;
}
