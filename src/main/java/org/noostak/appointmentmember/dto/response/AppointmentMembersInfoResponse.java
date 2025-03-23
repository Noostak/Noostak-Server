package org.noostak.appointmentmember.dto.response;

import java.util.List;

public record AppointmentMembersInfoResponse(
        Long memberId,
        String memberName,
        List<AppointmentMemberAvailableTimeResponse> appointmentMemberAvailableTimes
) {
    public static AppointmentMembersInfoResponse of(Long memberId, String memberName, AppointmentMemberAvailableTimesResponse appointmentMemberAvailableTimesResponse) {
        return new AppointmentMembersInfoResponse(memberId, memberName,
                appointmentMemberAvailableTimesResponse.appointmentMemberAvailableTimeResponses());
    }
}
