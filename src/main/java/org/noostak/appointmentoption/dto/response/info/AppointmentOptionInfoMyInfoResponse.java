package org.noostak.appointmentoption.dto.response.info;

public record AppointmentOptionInfoMyInfoResponse(String availability,
                                                  int position,
                                                  String name
) {
    public static AppointmentOptionInfoMyInfoResponse of(String availability, int position, String name) {
        return new AppointmentOptionInfoMyInfoResponse(availability, position, name);
    }
}
