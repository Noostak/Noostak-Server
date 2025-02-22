package org.noostak.appointmentmember.dto.request;

import java.time.LocalDateTime;

public record AppointmentMemberAvailableTimeRequest(
        LocalDateTime date,
        LocalDateTime startTime,
        LocalDateTime endTime
) {
    public static AppointmentMemberAvailableTimeRequest of(LocalDateTime date, LocalDateTime startTime, LocalDateTime endTime) {
        return new AppointmentMemberAvailableTimeRequest(date, startTime, endTime);
    }
}
