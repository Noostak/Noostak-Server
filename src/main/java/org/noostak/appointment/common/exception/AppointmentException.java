package org.noostak.appointment.common.exception;

import org.noostak.global.error.core.BaseException;

public class AppointmentException extends BaseException {
    public AppointmentException(AppointmentErrorCode errorCode) {
        super(errorCode);
    }
}
