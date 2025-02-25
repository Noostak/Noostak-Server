package org.noostak.appointment.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.noostak.global.error.core.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AppointmentErrorCode implements ErrorCode {
    APPOINTMENT_DURATION_NEGATIVE(HttpStatus.BAD_REQUEST, "약속 소요 시간은 음수가 될 수 없습니다."),
    APPOINTMENT_DURATION_MAX(HttpStatus.BAD_REQUEST, "약속 소요 시간은 최대 1440(24시간)분을 초과할 수 없습니다."),
    APPOINTMENT_DURATION_INVALID_UNIT(HttpStatus.BAD_REQUEST, "약속 소요 시간은 60분 단위로 입력해야 합니다."),

    APPOINTMENT_CATEGORY_NOT_FOUND(HttpStatus.BAD_REQUEST, "유효하지 않은 카테고리입니다."),
    APPOINTMENT_CATEGORY_NULL_OR_BLANK(HttpStatus.BAD_REQUEST, "약속 카테고리는 null이거나 공백일 수 없습니다."),

    APPOINTMENT_MEMBER_COUNT_NEGATIVE(HttpStatus.BAD_REQUEST, "약속 멤버 수는 음수가 될 수 없습니다."),
    APPOINTMENT_MEMBER_COUNT_MAX(HttpStatus.BAD_REQUEST, "약속 멤버 수는 최대 50명을 초과할 수 없습니다."),

    APPOINTMENT_NAME_NOT_EMPTY(HttpStatus.BAD_REQUEST, "약속 이름은 null이거나 공백일 수 없습니다."),
    INVALID_APPOINTMENT_NAME_LENGTH(HttpStatus.BAD_REQUEST, "약속 이름은 최대 50자를 초과할 수 없습니다."),
    INVALID_APPOINTMENT_NAME_CHARACTER(HttpStatus.BAD_REQUEST, "약속 이름은 한글, 영어, 숫자, 공백만 허용됩니다."),

    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 멤버를 찾을 수 없습니다."),
    GROUP_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 그룹을 찾을 수 없습니다."),
    ;

    public static final String PREFIX = "[APPOINTMENT ERROR] ";

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
