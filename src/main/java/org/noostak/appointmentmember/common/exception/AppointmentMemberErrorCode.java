package org.noostak.appointmentmember.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.noostak.global.error.core.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AppointmentMemberErrorCode implements ErrorCode {
    APPOINTMENT_MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 멤버가 약속에 존재하지 않습니다."),
    INVALID_AVAILABLE_TIME(HttpStatus.BAD_REQUEST, "올바른 시간을 입력해주세요."),
    DURATION_NOT_SATISFIED(HttpStatus.BAD_REQUEST, "입력한 시간이 약속의 진행 시간보다 작습니다."),
    APPOINTMENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 약속이 존재하지 않습니다."),
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 멤버가 존재하지 않습니다."),
    INVALID_TIME_ORDER(HttpStatus.BAD_REQUEST, "올바른 시간을 입력해주세요."),

    ;

    public static final String PREFIX = "[APPOINTMENT MEMBER ERROR] ";

    private final HttpStatus status;
    private final String rawMessage;

    @Override
    public String getMessage() {
        return PREFIX + rawMessage;
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }
}
