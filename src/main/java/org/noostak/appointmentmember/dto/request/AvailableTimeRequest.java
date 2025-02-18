package org.noostak.appointmentmember.dto.request;

import java.time.LocalDateTime;

public record AvailableTimeRequest(
        LocalDateTime date,
        LocalDateTime startTime,
        LocalDateTime endTime
) {
    public static AvailableTimeRequest of(LocalDateTime date, LocalDateTime startTime, LocalDateTime endTime) {
        return new AvailableTimeRequest(date, startTime, endTime);
    }
}
