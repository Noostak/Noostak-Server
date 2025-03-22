package org.noostak.appointmentmember.dto.response;

public record AppointmentMembersAvailableTimesResponse(
        boolean isAppointmentMemberTimeSet,
        AppointmentScheduleResponse appointmentSchedule
) {
    public static AppointmentMembersAvailableTimesResponse of(
            boolean isAppointMemberTimeSet,
            AppointmentScheduleResponse appointmentScheduleResponse
    ) {
        return new AppointmentMembersAvailableTimesResponse(isAppointMemberTimeSet, appointmentScheduleResponse);
    }
}
