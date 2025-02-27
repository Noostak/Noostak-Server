package org.noostak.appointment.common.success;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.noostak.global.success.SuccessCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CalendarSuccessCode implements SuccessCode {
    CALENDAR_CREATED(HttpStatus.CREATED, "캘린더 정보를 성공적으로 불러왔습니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
