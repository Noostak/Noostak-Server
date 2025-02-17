package org.noostak.appointment.dto.request;

import org.noostak.appointment.domain.AppointmentHostSelectionTimes;

import java.util.List;

public record AppointmentCreateRequest(
        String appointmentName,
        String category,
        Long duration,
        List<AppointmentHostSelectionTimes> appointmentHostSelectionTimes
) {
    public static AppointmentCreateRequest of(String appointmentName, String category, Long duration, List<AppointmentHostSelectionTimes> appointmentHostSelectionTimes) {
        return new AppointmentCreateRequest(appointmentName, category, duration, appointmentHostSelectionTimes);
    }
}
