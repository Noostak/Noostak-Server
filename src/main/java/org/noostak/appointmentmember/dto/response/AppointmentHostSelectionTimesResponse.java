package org.noostak.appointmentmember.dto.response;

import java.util.List;

public record AppointmentHostSelectionTimesResponse(
        List<AppointmentHostSelectionTimeResponse> appointmentHostSelectionTimeResponses
) {
    public static AppointmentHostSelectionTimesResponse of(List<AppointmentHostSelectionTimeResponse> appointmentHostSelectionTimeResponses) {
        return new AppointmentHostSelectionTimesResponse(appointmentHostSelectionTimeResponses);
    }
}
