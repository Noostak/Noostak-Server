package org.noostak.auth.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.noostak.global.error.core.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements ErrorCode {
    REFRESH_TOKEN_IS_NOT_NULL(HttpStatus.BAD_REQUEST,"refreshToken은 null이 될 수 없습니다."),
    AUTH_TYPE_NOT_EXISTS(HttpStatus.NOT_FOUND, "입력된 AuthType을 찾을 수 없습니다. 입력값 : %s"),
    AUTH_INFO_NOT_EXISTS(HttpStatus.NOT_FOUND, "입력된 AuthId를 찾을 수 없습니다. 입력값 : %s"),
    AUTHID_ALREADY_EXISTS(HttpStatus.NOT_FOUND, "동일한 AuthId가 존재합니다. 입력값 : %s"),
    API_ERROR_RESPONSE(HttpStatus.BAD_REQUEST, "외부 API 호출 도중 에러가 발생하였습니다. 에러내용: %s"),
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "접근 권한이 없습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다."),
    EMPTY_TOKEN(HttpStatus.UNAUTHORIZED, "토큰 값이 비어있습니다."),

    ;

    public static final String PREFIX = "[AUTH ERROR] ";

    private final HttpStatus status;
    private final String rawMessage;

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return PREFIX + rawMessage;
    }
}
