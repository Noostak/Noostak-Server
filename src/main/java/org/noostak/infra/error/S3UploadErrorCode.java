package org.noostak.infra.error;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum S3UploadErrorCode {
    INVALID_EXTENSION(HttpStatus.BAD_REQUEST, "이미지 확장자는 jpg, png, webp만 가능합니다."),
    FILE_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "이미지 사이즈는 5MB를 넘을 수 없습니다.");

    public static final String PREFIX = "[S3 UPLOAD ERROR] ";

    private final HttpStatus status;
    private final String rawMessage;

    public String getMessage() {
        return PREFIX + rawMessage;
    }

    public int getStatusValue() {
        return status.value();
    }
}