package org.noostak.appointmentmember.common.success;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.noostak.global.success.SuccessCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AppointmentMemberSuccessCode implements SuccessCode {
    SUCCESS_SAVE_AVAILABLE_TIMES(HttpStatus.OK, "약속 시간이 성공적으로 저장되었습니다.")
    ;

    private final HttpStatus status;
    private final String message;
}
