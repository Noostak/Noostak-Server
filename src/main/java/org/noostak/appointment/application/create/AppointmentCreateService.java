package org.noostak.appointment.application.create;

import org.noostak.appointment.dto.request.AppointmentCreateRequest;

public interface AppointmentCreateService {
    void createAppointment(Long memberId, Long groupId, AppointmentCreateRequest request);
}
