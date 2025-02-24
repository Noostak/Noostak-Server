package org.noostak.appointment.application;

import org.noostak.appointment.dto.request.AppointmentCreateRequest;

public interface AppointmentCreateService {
    void createAppointment(Long memberId, Long groupId, AppointmentCreateRequest request);
}
