package org.noostak.appointmentmember.dto.response;

import java.util.List;

public record AppointmentScheduleResponse(
        Long duration,
        List<AppointmentHostSelectionTimeResponse> appointmentHostSelectionTimes,
        List<AppointmentMembersInfoResponse> appointmentMembersInfo
) {
    public static AppointmentScheduleResponse of(
            Long duration,
            AppointmentHostSelectionTimesResponse appointmentHostSelectionTimesResponse,
            List<AppointmentMembersInfoResponse> appointmentMembersInfoResponse
    ) {
        return new AppointmentScheduleResponse(
                duration,
                appointmentHostSelectionTimesResponse.appointmentHostSelectionTimeResponses(),
                appointmentMembersInfoResponse);
    }
}
