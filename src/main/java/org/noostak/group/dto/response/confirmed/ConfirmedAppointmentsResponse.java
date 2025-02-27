package org.noostak.group.dto.response.confirmed;

public record ConfirmedAppointmentsResponse(
        Long appointmentId,
        String appointmentName,
        String category,
        AppointmentTimeResponse appointmentTime
) {
    public static ConfirmedAppointmentsResponse of(Long appointmentId, String appointmentName, String category, AppointmentTimeResponse appointmentTime) {
        return new ConfirmedAppointmentsResponse(appointmentId, appointmentName, category, appointmentTime);
    }
}
