package org.noostak.group.application.ongoing;

import org.noostak.group.dto.response.ongoing.GroupOngoingAppointmentsResponse;

public interface GroupOngoingAppointmentService {
    GroupOngoingAppointmentsResponse getGroupOngoingAppointments(Long memberId, Long groupId);
}
