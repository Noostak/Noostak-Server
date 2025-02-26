package org.noostak.group.dto.response.ongoing;

import java.util.List;

public record GroupOngoingAppointmentsResponse(
        GroupOngoingInfoResponse groupOngoingInfo,
        List<OngoingAppointmentResponse> ongoingAppointments
) {
    public static GroupOngoingAppointmentsResponse of(
            GroupOngoingInfoResponse groupOngoingInfo,
            List<OngoingAppointmentResponse> ongoingAppointments
    ) {
        return new GroupOngoingAppointmentsResponse(groupOngoingInfo, ongoingAppointments);
    }
}
