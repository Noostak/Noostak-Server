package org.noostak.appointment.application;

import lombok.RequiredArgsConstructor;
import org.noostak.appointment.dto.request.AppointmentCreateRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentCreateService appointmentCreateService;

    public void createAppointment(Long memberId, Long groupId, AppointmentCreateRequest request) {
        appointmentCreateService.createAppointment(memberId, groupId, request);
    }
}
