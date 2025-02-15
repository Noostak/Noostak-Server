package org.noostak.group.common.success;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.noostak.global.success.SuccessCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GroupSuccessCode implements SuccessCode {
    GROUP_CREATED(HttpStatus.CREATED, "그룹이 성공적으로 생성되었습니다."),

    GROUP_RETRIEVED(HttpStatus.OK, "그룹이 성공적으로 조회되었습니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
