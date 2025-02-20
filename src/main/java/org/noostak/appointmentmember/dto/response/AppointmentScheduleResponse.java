package org.noostak.appointmentmember.dto.response;

import java.util.List;

public record AppointmentScheduleResponse(
        AppointmentHostSelectionTimesResponse appointmentHostSelectionTimesResponse,
        List<AppointmentMemberInfoResponse> appointmentMemberInfoResponse
) {
    public static AppointmentScheduleResponse of(
            AppointmentHostSelectionTimesResponse appointmentHostSelectionTimesResponse,
            List<AppointmentMemberInfoResponse> appointmentMemberInfoResponse
    ) {
        return new AppointmentScheduleResponse(appointmentHostSelectionTimesResponse, appointmentMemberInfoResponse);
    }
}
