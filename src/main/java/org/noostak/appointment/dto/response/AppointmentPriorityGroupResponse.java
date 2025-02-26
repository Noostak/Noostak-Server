package org.noostak.appointment.dto.response;

import java.util.List;

public record AppointmentPriorityGroupResponse(
        Long priority,
        List<AppointmentOptionResponse> options
) {
    public static AppointmentPriorityGroupResponse of(Long priority, List<AppointmentOptionResponse> options) {
        return new AppointmentPriorityGroupResponse(priority, options);
    }
}
