package org.noostak.appointment.dto.response;

import java.util.List;

public record AppointmentRecommendedOptionsResponse(
        boolean isAppointmentHost,
        List<AppointmentPriorityGroupResponse> priorities
) {
    public static AppointmentRecommendedOptionsResponse of(boolean isAppointmentHost, List<AppointmentPriorityGroupResponse> priorities) {
        return new AppointmentRecommendedOptionsResponse(isAppointmentHost, priorities);
    }
}
