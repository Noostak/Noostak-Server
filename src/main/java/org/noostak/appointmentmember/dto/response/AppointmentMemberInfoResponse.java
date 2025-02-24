package org.noostak.appointmentmember.dto.response;

public record AppointmentMemberInfoResponse(
        Long memberId,
        String memberName,
        AppointmentMemberAvailableTimesResponse appointmentMemberAvailableTimesResponse
) {
    public static AppointmentMemberInfoResponse of(Long memberId, String memberName, AppointmentMemberAvailableTimesResponse appointmentMemberAvailableTimesResponse) {
        return new AppointmentMemberInfoResponse(memberId, memberName, appointmentMemberAvailableTimesResponse);
    }
}
