package org.noostak.appointmentoption.dto.response.confirmed;

import java.util.List;

public record AppointmentConfirmedOptionAvailableFriendsResponse(
        int count,
        List<String> names
) {
    public static AppointmentConfirmedOptionAvailableFriendsResponse of(int count, List<String> names) {
        return new AppointmentConfirmedOptionAvailableFriendsResponse(count, names);
    }
}
