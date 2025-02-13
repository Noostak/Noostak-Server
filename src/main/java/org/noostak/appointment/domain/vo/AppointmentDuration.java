package org.noostak.appointment.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import org.noostak.appointment.common.exception.AppointmentErrorCode;
import org.noostak.appointment.common.exception.AppointmentException;

@Embeddable
@EqualsAndHashCode
public class AppointmentDuration {

    private final Long duration;

    protected AppointmentDuration() {
        this.duration = 0L;
    }

    private AppointmentDuration(Long duration) {
        validate(duration);
        this.duration = duration;
    }

    public static AppointmentDuration from(Long duration) {
        return new AppointmentDuration(duration);
    }

    public Long value() {
        return duration;
    }

    private void validate(Long duration) {
        validateNonNegative(duration);
        validateMaxLimit(duration);
        validateMultipleOfSixty(duration);
    }

    private void validateNonNegative(Long duration) {
        if (duration < 0) {
            throw new AppointmentException(AppointmentErrorCode.APPOINTMENT_DURATION_NEGATIVE);
        }
    }

    private void validateMaxLimit(Long duration) {
        if (duration > 1440) {
            throw new AppointmentException(AppointmentErrorCode.APPOINTMENT_DURATION_MAX);
        }
    }

    private void validateMultipleOfSixty(Long duration) {
        if (duration % 60 != 0) {
            throw new AppointmentException(AppointmentErrorCode.APPOINTMENT_DURATION_INVALID_UNIT);
        }
    }
}
