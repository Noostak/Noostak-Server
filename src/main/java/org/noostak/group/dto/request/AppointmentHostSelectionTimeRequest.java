package org.noostak.group.dto.request;

import java.time.LocalDateTime;

public record AppointmentHostSelectionTimeRequest(
        LocalDateTime date,
        LocalDateTime startTime,
        LocalDateTime endTime
) {
    public static AppointmentHostSelectionTimeRequest of(LocalDateTime date, LocalDateTime startTime, LocalDateTime endTime) {
        return new AppointmentHostSelectionTimeRequest(date, startTime, endTime);
    }
}
