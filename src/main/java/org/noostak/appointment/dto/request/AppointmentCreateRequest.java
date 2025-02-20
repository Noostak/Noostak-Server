package org.noostak.appointment.dto.request;

import java.util.List;

public record AppointmentCreateRequest(
        String appointmentName,
        String category,
        Long duration,
        List<AppointmentHostSelectionTimeRequest> appointmentHostSelectionTimes
) {
    public static AppointmentCreateRequest of(String appointmentName, String category, Long duration, List<AppointmentHostSelectionTimeRequest> appointmentHostSelectionTimes) {
        return new AppointmentCreateRequest(appointmentName, category, duration, appointmentHostSelectionTimes);
    }
}
