package org.noostak.likes.common.success;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.noostak.global.success.SuccessCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum LikesSuccessCode implements SuccessCode {
    LIKE_CREATED(HttpStatus.CREATED, "좋아요가 생성되었습니다."),

    ;

    private final HttpStatus status;
    private final String rawMessage;

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return rawMessage;
    }
}
