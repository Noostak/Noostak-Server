package org.noostak.appointment.dto.response;

public record AppointmentMyInfoResponse(
        String availability,
        Long position,
        String name
) {
    public static AppointmentMyInfoResponse of(String availability, Long position, String name) {
        return new AppointmentMyInfoResponse(availability, position, name);
    }
}
