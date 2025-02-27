package org.noostak.appointmentoption.dto.response.confirmed;

import java.time.LocalDateTime;

public record AppointmentConfirmedOptionTimeResponse(
        LocalDateTime date,
        LocalDateTime startTime,
        LocalDateTime endTime
) {
    public static AppointmentConfirmedOptionTimeResponse of(LocalDateTime date, LocalDateTime startTime, LocalDateTime endTime) {
        return new AppointmentConfirmedOptionTimeResponse(date, startTime, endTime);
    }
}
