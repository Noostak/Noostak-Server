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

    GROUP_INFO(HttpStatus.OK, "그룹 정보가 성공적으로 조회되었습니다."),

    GROUP_ONGOING_APPOINTMENTS_LOADED(HttpStatus.OK, "그룹의 진행중인 약속이 성공적으로 조회되었습니다.")
    ;

    private final HttpStatus status;
    private final String message;
}
