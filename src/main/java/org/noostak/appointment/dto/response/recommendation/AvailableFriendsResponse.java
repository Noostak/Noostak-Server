package org.noostak.appointment.dto.response.recommendation;

import java.util.List;

public record AvailableFriendsResponse(
        Long count,
        List<String> names
) {
    public static AvailableFriendsResponse of(Long count, List<String> names) {
        return new AvailableFriendsResponse(count, names);
    }
}
