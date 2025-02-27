package org.noostak.appointmentoption.dto.response.confirmed;

import java.util.List;

public record AppointmentConfirmedOptionUnavailableFriendsResponse(
        int count,
        List<String> names
) {
    public static AppointmentConfirmedOptionUnavailableFriendsResponse of(int count, List<String> names) {
        return new AppointmentConfirmedOptionUnavailableFriendsResponse(count, names);
    }
}
