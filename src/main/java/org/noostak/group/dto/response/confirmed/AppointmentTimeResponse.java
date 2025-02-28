package org.noostak.group.dto.response.confirmed;

import java.time.LocalDateTime;

public record AppointmentTimeResponse(
        LocalDateTime date,
        LocalDateTime startTime,
        LocalDateTime endTime
) {
    public static AppointmentTimeResponse of(LocalDateTime date, LocalDateTime startTime, LocalDateTime endTime) {
        return new AppointmentTimeResponse(date, startTime, endTime);
    }
}
