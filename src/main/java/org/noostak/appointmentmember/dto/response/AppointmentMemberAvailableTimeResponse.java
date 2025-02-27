package org.noostak.appointmentmember.dto.response;

import java.time.LocalDateTime;

public record AppointmentMemberAvailableTimeResponse(
        LocalDateTime date,
        LocalDateTime startTime,
        LocalDateTime endTime
) {
    public static AppointmentMemberAvailableTimeResponse of(LocalDateTime date, LocalDateTime startTime, LocalDateTime endTime) {
        return new AppointmentMemberAvailableTimeResponse(date, startTime, endTime);
    }
}
