package org.noostak.appointment.dto.response.recommendation;

import java.time.LocalDateTime;
import java.util.List;

public record AppointmentOptionAvailabilityResponse(
        LocalDateTime date,
        LocalDateTime startTime,
        LocalDateTime endTime,
        List<Long> availableMembers
) {
    public static AppointmentOptionAvailabilityResponse of(LocalDateTime date, LocalDateTime startTime, LocalDateTime endTime, List<Long> availableMembers) {
        return new AppointmentOptionAvailabilityResponse(date, startTime, endTime, availableMembers);
    }
}
