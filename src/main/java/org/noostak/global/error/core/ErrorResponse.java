package org.noostak.global.error.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private final HttpStatus status;
    private final String message;

    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.getStatus(), errorCode.getMessage());
    }

    public static ErrorResponse of(ErrorCode errorCode, Object ... args) {
        return new ErrorResponse(errorCode.getStatus(), errorCode.getMessage(args));
    }
}
