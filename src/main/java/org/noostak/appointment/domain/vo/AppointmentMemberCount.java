package org.noostak.appointment.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import org.noostak.appointment.common.exception.AppointmentErrorCode;
import org.noostak.appointment.common.exception.AppointmentException;

@Embeddable
@EqualsAndHashCode
public class AppointmentMemberCount {
    private static final Long MAX_MEMBERS = 50L;
    private final Long count;

    protected AppointmentMemberCount() {
        this.count = 0L;
    }

    private AppointmentMemberCount(Long count) {
        validateAppointmentMemberCount(count);
        this.count = count;
    }

    public static AppointmentMemberCount from(Long count) {
        return new AppointmentMemberCount(count);
    }

    public AppointmentMemberCount increase() {
        return new AppointmentMemberCount(count + 1);
    }

    public AppointmentMemberCount decrease() {
        return new AppointmentMemberCount(count - 1);
    }

    public Long value() {
        return count;
    }

    private void validateAppointmentMemberCount(Long count) {
        validateNonNegative(count);
        validateMaxLimit(count);
    }

    private void validateNonNegative(Long count) {
        if (count < 0) {
            throw new AppointmentException(AppointmentErrorCode.APPOINTMENT_MEMBER_COUNT_NEGATIVE);
        }
    }

    private void validateMaxLimit(Long count) {
        if (count > MAX_MEMBERS) {
            throw new AppointmentException(AppointmentErrorCode.APPOINTMENT_MEMBER_COUNT_MAX);
        }
    }

    @Override
    public String toString() {
        return String.valueOf(count);
    }
}
