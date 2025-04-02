package org.noostak.appointmentoption.dto.response.info;

import java.time.LocalDateTime;

public record AppointmentOptionInfoTimeResponse(LocalDateTime date,
                                                LocalDateTime startTime,
                                                LocalDateTime endTime
) {
    public static AppointmentOptionInfoTimeResponse of(LocalDateTime date, LocalDateTime startTime, LocalDateTime endTime) {
        return new AppointmentOptionInfoTimeResponse(date, startTime, endTime);
    }
}
