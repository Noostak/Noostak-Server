package org.noostak.appointmentmember.dto.response;

import java.util.List;

public record AppointmentMemberAvailableTimesResponse(
        List<AppointmentMemberAvailableTimeResponse> appointmentMemberAvailableTimesResponseResponse
) {
    public static AppointmentMemberAvailableTimesResponse of(List<AppointmentMemberAvailableTimeResponse> appointmentMemberAvailableTimesResponseResponse) {
        return new AppointmentMemberAvailableTimesResponse(appointmentMemberAvailableTimesResponseResponse);
    }
}
