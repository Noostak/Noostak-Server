package org.noostak.appointmentoption.dto.response.confirmed;

public record AppointmentConfirmedOptionMyInfoResponse(
        String availability,
        int position,
        String name
) {
    public static AppointmentConfirmedOptionMyInfoResponse of(String availability, int position, String name) {
        return new AppointmentConfirmedOptionMyInfoResponse(availability, position, name);
    }
}
