package org.noostak.appointmentmember.dto.response;

public record AppointmentMembersAvailableTimesResponse(
        boolean isAppointMemberTimeSet,
        AppointmentScheduleResponse appointmentScheduleResponse
) {
    public static AppointmentMembersAvailableTimesResponse of(
            boolean isAppointMemberTimeSet,
            AppointmentScheduleResponse appointmentScheduleResponse
    ) {
        return new AppointmentMembersAvailableTimesResponse(isAppointMemberTimeSet, appointmentScheduleResponse);
    }
}
