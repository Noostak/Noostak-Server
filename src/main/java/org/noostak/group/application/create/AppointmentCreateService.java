package org.noostak.group.application.create;

import org.noostak.group.dto.request.AppointmentCreateRequest;

public interface AppointmentCreateService {
    void createAppointment(Long memberId, Long groupId, AppointmentCreateRequest request);
}
