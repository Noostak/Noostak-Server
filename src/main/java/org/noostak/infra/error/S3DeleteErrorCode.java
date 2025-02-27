package org.noostak.infra.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.noostak.global.error.core.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum S3DeleteErrorCode implements ErrorCode {
    IMAGE_DELETE_FAILED(HttpStatus.BAD_REQUEST, "이미지 삭제에 실패 하였습니다. 에러 원인: %s")

    ;

    public static final String PREFIX = "[S3 DELETE ERROR] ";

    private final HttpStatus status;
    private final String rawMessage;

    public String getMessage() {
        return PREFIX + rawMessage;
    }

    public int getStatusValue() {
        return status.value();
    }
}