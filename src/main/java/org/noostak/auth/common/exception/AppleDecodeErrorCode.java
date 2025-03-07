package org.noostak.auth.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.noostak.global.error.core.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AppleDecodeErrorCode implements ErrorCode {
    DECODE_ERROR(HttpStatus.BAD_REQUEST, "Apple Decode 도중 에러가 발생하였습니다. 에러 내용 %s"),

    ;

    public static final String PREFIX = "[APPLE ERROR] ";

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
