package org.noostak.group.dto.response.ongoing;

import java.time.LocalDateTime;

public record AppointmentOngoingHostSelectionTimeResponse(
        LocalDateTime date,
        LocalDateTime startTime,
        LocalDateTime endTime
) {
    public static AppointmentOngoingHostSelectionTimeResponse of(
            LocalDateTime date,
            LocalDateTime startTime,
            LocalDateTime endTime
    ) {
        return new AppointmentOngoingHostSelectionTimeResponse(date, startTime, endTime);
    }
}
