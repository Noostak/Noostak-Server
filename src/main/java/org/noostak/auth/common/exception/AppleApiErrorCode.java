package org.noostak.auth.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.noostak.global.error.core.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AppleApiErrorCode implements ErrorCode {
    APPLE_API_ERROR(HttpStatus.NOT_FOUND, "애플 API 호출에 대해 에러 응답이 반환 되었습니다. 에러 정보 : %s"),

    ;

    public static final String PREFIX = "[APPLE API ERROR] ";

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
