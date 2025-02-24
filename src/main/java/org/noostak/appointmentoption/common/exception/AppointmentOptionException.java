package org.noostak.appointmentoption.common.exception;

import org.noostak.global.error.core.BaseException;

public class AppointmentOptionException extends BaseException {
    public AppointmentOptionException(AppointmentOptionErrorCode message) {
        super(message);
    }
}
