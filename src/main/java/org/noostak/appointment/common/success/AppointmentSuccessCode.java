package org.noostak.appointment.common.success;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.noostak.global.success.SuccessCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AppointmentSuccessCode implements SuccessCode {
    APPOINTMENT_CREATED(HttpStatus.CREATED, "약속이 성공적으로 생성되었습니다."),

    APPOINTMENT_CONFIRMED(HttpStatus.OK, "약속이 성공적으로 확정되었습니다."),

    CONFIRMED_APPOINTMENT_RETRIEVED(HttpStatus.OK, "확정된 약속이 성공적으로 조회되었습니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
