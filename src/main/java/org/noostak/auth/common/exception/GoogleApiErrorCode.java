package org.noostak.auth.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.noostak.global.error.core.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GoogleApiErrorCode implements ErrorCode {
    GOOGLE_API_ERROR(HttpStatus.NOT_FOUND, "구글 API 호출에 대해 에러 응답이 반환 되었습니다. 에러 정보 : %s"),

    ;

    public static final String PREFIX = "[GOOGLE API ERROR] ";

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
