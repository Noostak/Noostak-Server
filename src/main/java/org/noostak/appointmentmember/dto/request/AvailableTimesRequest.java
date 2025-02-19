package org.noostak.appointmentmember.dto.request;

import java.util.List;

public record AvailableTimesRequest(
        List<AvailableTimeRequest> appointmentMemberAvailableTimes
) {
    public static AvailableTimesRequest of(List<AvailableTimeRequest> appointmentMemberAvailableTimes) {
        return new AvailableTimesRequest(appointmentMemberAvailableTimes);
    }
}
