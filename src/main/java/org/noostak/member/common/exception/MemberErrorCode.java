package org.noostak.member.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.noostak.global.error.core.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberErrorCode implements ErrorCode {
    MEMBER_NAME_NOT_EMPTY(HttpStatus.BAD_REQUEST, "멤버 이름은 비어 있을 수 없습니다."),
    MEMBER_NAME_LENGTH_EXCEEDED(HttpStatus.BAD_REQUEST, "멤버 이름의 길이는 15글자를 넘을 수 없습니다."),
    INVALID_MEMBER_NAME(HttpStatus.BAD_REQUEST, "멤버 이름에는 한글, 영문, 이모지만 포함될 수 있습니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "멤버를 찾을 수 없습니다."),

    MEMBER_PROFILE_IMAGE_KEY_NOT_EMPTY(HttpStatus.BAD_REQUEST, "멤버 프로필 이미지 키는 비어 있을 수 없습니다."),

    AUTH_ID_NOT_EMPTY(HttpStatus.BAD_REQUEST, "인증 ID는 비어 있을 수 없습니다."),
    AUTH_ID_NOT_NULL(HttpStatus.BAD_REQUEST, "인증 ID는 null일 수 없습니다."),
    MEMBER_PROFILE_UPLOAD_FAIL(HttpStatus.BAD_REQUEST, "멤버 이미지 업로드에 실패하였습니다."),

    ;

    public static final String PREFIX = "[MEMBER ERROR] ";

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
