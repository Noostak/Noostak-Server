package org.noostak.appointment.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import org.noostak.appointment.common.exception.AppointmentErrorCode;
import org.noostak.appointment.common.exception.AppointmentException;

import java.util.regex.Pattern;

@Embeddable
@EqualsAndHashCode
public class AppointmentName {
    private static final int MAX_LENGTH = 20;

    private final String name;

    protected AppointmentName() {
        this.name = null;
    }

    private AppointmentName(String name) {
        validateAppointmentName(name);
        this.name = name;
    }

    public static AppointmentName from(String name) {
        return new AppointmentName(name);
    }

    public String value() {
        return name;
    }

    private void validateAppointmentName(String name) {
        validateNotEmptyOrBlank(name);
        validateLength(name);
    }

    private void validateNotEmptyOrBlank(String name) {
        if (name == null || name.isBlank()) {
            throw new AppointmentException(AppointmentErrorCode.APPOINTMENT_NAME_NOT_EMPTY);
        }
    }

    private void validateLength(String name) {
        if (name.length() > MAX_LENGTH) {
            throw new AppointmentException(AppointmentErrorCode.INVALID_APPOINTMENT_NAME_LENGTH);
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
