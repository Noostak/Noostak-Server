package org.noostak.appointmentoption.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.noostak.global.error.core.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AppointmentOptionErrorCode implements ErrorCode {
    APPOINTMENT_OPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 ID의 옵션을 찾을 수 없습니다."),
    ;

    public static final String PREFIX = "[APPOINTMENT OPTION ERROR] ";

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
