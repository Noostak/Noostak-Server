package org.noostak.appointmentmember.dto.response;

import java.util.List;

public record AppointmentScheduleResponse(
        List<AppointmentHostSelectionTimeResponse> appointmentHostSelectionTimes,
        List<AppointmentMembersInfoResponse> appointmentMembersInfo
) {
    public static AppointmentScheduleResponse of(
            AppointmentHostSelectionTimesResponse appointmentHostSelectionTimesResponse,
            List<AppointmentMembersInfoResponse> appointmentMembersInfoResponse
    ) {
        return new AppointmentScheduleResponse(
                appointmentHostSelectionTimesResponse.appointmentHostSelectionTimeResponses(),
                appointmentMembersInfoResponse);
    }
}
