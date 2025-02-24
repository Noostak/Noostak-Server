package org.noostak.group.dto.response.ongoing;

import java.util.List;

public record OngoingAppointmentResponse(
        Long appointmentId,
        String appointmentName,
        Long availableGroupMemberCount,
        List<AppointmentOngoingHostSelectionTimeResponse> hostSelectionTimes
) {
    public static OngoingAppointmentResponse of(
            Long appointmentId,
            String appointmentName,
            Long availableGroupMemberCount
    ) {
        return new OngoingAppointmentResponse(appointmentId, appointmentName, availableGroupMemberCount);
    }
}

