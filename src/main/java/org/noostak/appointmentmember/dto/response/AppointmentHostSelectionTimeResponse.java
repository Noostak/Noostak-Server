package org.noostak.appointmentmember.dto.response;

import java.time.LocalDateTime;

public record AppointmentHostSelectionTimeResponse(
        LocalDateTime date,
        LocalDateTime startTime,
        LocalDateTime endTime
) {
    public static AppointmentHostSelectionTimeResponse of(LocalDateTime date, LocalDateTime startTime, LocalDateTime endTime) {
        return new AppointmentHostSelectionTimeResponse(date, startTime, endTime);
    }
}
