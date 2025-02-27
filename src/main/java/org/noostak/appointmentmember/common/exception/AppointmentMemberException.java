package org.noostak.appointmentmember.common.exception;

import org.noostak.global.error.core.BaseException;
import org.noostak.global.error.core.ErrorCode;

public class AppointmentMemberException extends BaseException {
    public AppointmentMemberException(ErrorCode errorCode) {
        super(errorCode);
    }
}
