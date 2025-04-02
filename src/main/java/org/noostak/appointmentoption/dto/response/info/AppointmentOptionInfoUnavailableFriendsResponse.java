package org.noostak.appointmentoption.dto.response.info;

import java.util.List;

public record AppointmentOptionInfoUnavailableFriendsResponse(int count,
                                                              List<String> names
) {
    public static AppointmentOptionInfoUnavailableFriendsResponse of(int count, List<String> names) {
        return new AppointmentOptionInfoUnavailableFriendsResponse(count, names);
    }
}

