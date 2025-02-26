package org.noostak.likes.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.noostak.global.error.core.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum LikesErrorCode implements ErrorCode {
    LIKES_NOT_NEGATIVE(HttpStatus.BAD_REQUEST, "좋아요 수는 음수가 될 수 없습니다."),
    OVER_MAX_LIKES(HttpStatus.BAD_REQUEST, "좋아요 수는 최대 %d을 초과할 수 없습니다."),

    ;

    public static final String PREFIX = "[LIKES ERROR] ";

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
