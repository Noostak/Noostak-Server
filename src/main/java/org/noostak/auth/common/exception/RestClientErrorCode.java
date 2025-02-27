package org.noostak.auth.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.noostak.global.error.core.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum RestClientErrorCode implements ErrorCode {
    REST_CLIENT_ERROR(HttpStatus.BAD_REQUEST, "RestClient 에서 예기치않은 에러가 발생하였습니다. 에러내용: %s"),
    RESPONSE_IS_NULL(HttpStatus.BAD_REQUEST, "RestClient의 응답이 비어있습니다."),

    ;

    public static final String PREFIX = "[REST CLIENT ERROR] ";

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
