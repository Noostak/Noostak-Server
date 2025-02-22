package org.noostak.auth.common.success;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.noostak.global.success.SuccessCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthSuccessCode implements SuccessCode {
    SIGNUP_COMPLETED(HttpStatus.CREATED, "회원가입이 성공적으로 완료 되었습니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
