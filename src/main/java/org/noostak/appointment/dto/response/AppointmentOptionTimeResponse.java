package org.noostak.appointment.dto.response;

import java.time.LocalDateTime;

public record AppointmentOptionTimeResponse(
        LocalDateTime date,
        LocalDateTime startTime,
        LocalDateTime endTime
) {
    public static AppointmentOptionTimeResponse of(LocalDateTime date, LocalDateTime startTime, LocalDateTime endTime) {
        return new AppointmentOptionTimeResponse(date, startTime, endTime);
    }
}
