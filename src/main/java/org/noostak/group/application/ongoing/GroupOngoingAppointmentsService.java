package org.noostak.group.application.ongoing;

import org.noostak.group.dto.response.ongoing.GroupOngoingAppointmentsResponse;

public interface GroupOngoingAppointmentsService {
    GroupOngoingAppointmentsResponse getGroupOngoingAppointments(Long groupId);
}
