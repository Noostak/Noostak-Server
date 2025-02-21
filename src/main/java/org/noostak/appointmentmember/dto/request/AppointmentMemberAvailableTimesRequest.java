package org.noostak.appointmentmember.dto.request;

import java.util.List;

public record AppointmentMemberAvailableTimesRequest(
        List<AppointmentMemberAvailableTimeRequest> appointmentMemberAvailableTimes
) {
    public static AppointmentMemberAvailableTimesRequest of(List<AppointmentMemberAvailableTimeRequest> appointmentMemberAvailableTimes) {
        return new AppointmentMemberAvailableTimesRequest(appointmentMemberAvailableTimes);
    }
}
