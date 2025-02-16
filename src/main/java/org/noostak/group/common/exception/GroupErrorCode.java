package org.noostak.group.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.noostak.global.error.core.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GroupErrorCode implements ErrorCode {
    INVITE_CODE_NOT_EMPTY(HttpStatus.BAD_REQUEST, "그룹 초대 코드는 비어 있을 수 없습니다."),
    INVALID_INVITE_CODE_LENGTH(HttpStatus.BAD_REQUEST, "그룹 초대 코드의 길이는 6글자여야 합니다."),
    INVALID_INVITE_CODE_ALPHA_NUMERIC_ONLY(HttpStatus.BAD_REQUEST, "그룹 초대 코드는 알파벳 대문자와 숫자로만 구성되어야 합니다."),

    GROUP_NAME_NOT_EMPTY(HttpStatus.BAD_REQUEST, "그룹 이름은 비어 있을 수 없습니다."),
    INVALID_GROUP_NAME_LENGTH(HttpStatus.BAD_REQUEST, "그룹 이름의 길이는 30글자를 넘을 수 없습니다."),
    INVALID_GROUP_NAME_CHARACTER(HttpStatus.BAD_REQUEST, "그룹 이름은 한글, 영문 대소문자, 숫자로만 구성되어야 합니다."),

    MEMBER_COUNT_INITIAL_NEGATIVE(HttpStatus.BAD_REQUEST, "초기 그룹 멤버 수는 음수가 될 수 없습니다."),
    MEMBER_COUNT_NEGATIVE(HttpStatus.BAD_REQUEST, "그룹 멤버 수는 음수가 될 수 없습니다."),
    MEMBER_COUNT_EXCEEDS_MAX_LIMIT(HttpStatus.BAD_REQUEST, "그룹 멤버 수는 최대 50명을 초과할 수 없습니다."),

    HOST_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "호스트 멤버를 찾을 수 없습니다."),
    GROUP_PROFILE_IMAGE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "그룹 프로필 이미지 업로드에 실패했습니다."),
    GROUP_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "그룹 생성에 실패했습니다."),
    GROUP_PROFILE_IMAGE_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "그룹 프로필 이미지 삭제에 실패했습니다."),

    GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "그룹을 찾을 수 없습니다."),
    GROUP_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "그룹 멤버를 찾을 수 없습니다."),
    ;

    public static final String PREFIX = "[GROUP ERROR] ";

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
