package org.noostak.appointmentoption.dto.response.info;

import java.util.List;

public record AppointmentOptionInfoAvailableFriendsResponse(int count,
                                                            List<String> names
) {
    public static AppointmentOptionInfoAvailableFriendsResponse of(int count, List<String> names) {
        return new AppointmentOptionInfoAvailableFriendsResponse(count, names);
    }
}
