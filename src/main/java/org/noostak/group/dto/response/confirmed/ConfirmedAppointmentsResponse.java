package org.noostak.group.dto.response.confirmed;

public record ConfirmedAppointmentsResponse(
        Long appointmentId,
        Long appointmentOptionId,
        String appointmentName,
        String category,
        AppointmentTimeResponse appointmentTime
) {
    public static ConfirmedAppointmentsResponse of(Long appointmentId, Long appointmentOptionId, String appointmentName, String category, AppointmentTimeResponse appointmentTime) {
        return new ConfirmedAppointmentsResponse(appointmentId, appointmentOptionId, appointmentName, category, appointmentTime);
    }
}
