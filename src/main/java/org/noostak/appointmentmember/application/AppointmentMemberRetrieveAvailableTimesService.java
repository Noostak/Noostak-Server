package org.noostak.appointmentmember.application;

import org.noostak.appointmentmember.dto.response.AppointmentMembersAvailableTimesResponse;

public interface AppointmentMemberRetrieveAvailableTimesService {
    AppointmentMembersAvailableTimesResponse retrieveAvailableTimes(Long memberId, Long appointmentId);
}
