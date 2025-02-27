package org.noostak.appointment.dto.response.recommendation;

import java.util.List;

public record UnavailableFriendsResponse(
        Long count,
        List<String> names
) {
    public static UnavailableFriendsResponse of(Long count, List<String> names) {
        return new UnavailableFriendsResponse(count, names);
    }
}
