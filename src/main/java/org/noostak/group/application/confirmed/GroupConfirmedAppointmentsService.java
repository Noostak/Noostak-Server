package org.noostak.group.application.confirmed;

import org.noostak.group.dto.response.confirmed.GroupConfirmedAppointmentsResponse;

public interface GroupConfirmedAppointmentsService {
    GroupConfirmedAppointmentsResponse getGroupConfirmedAppointments(Long memberId, Long groupId);
}
