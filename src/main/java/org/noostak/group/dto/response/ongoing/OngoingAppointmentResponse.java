package org.noostak.group.dto.response.ongoing;

public record OngoingAppointmentResponse(
        Long appointmentId,
        String appointmentName,
        Long availableGroupMemberCount
) {
    public static OngoingAppointmentResponse of(
            Long appointmentId,
            String appointmentName,
            Long availableGroupMemberCount
    ) {
        return new OngoingAppointmentResponse(appointmentId, appointmentName, availableGroupMemberCount);
    }
}

