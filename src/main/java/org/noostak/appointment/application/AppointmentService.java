package org.noostak.appointment.application;

import lombok.RequiredArgsConstructor;
import org.noostak.appointment.application.create.AppointmentCreateService;
import org.noostak.appointment.application.recommendation.AppointmentRecommendedOptionService;
import org.noostak.appointment.dto.request.AppointmentCreateRequest;
import org.noostak.appointment.dto.response.AppointmentRecommendedOptionsResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentCreateService appointmentCreateService;
    private final AppointmentRecommendedOptionService appointmentRecommendedOptionService;

    public void createAppointment(Long memberId, Long groupId, AppointmentCreateRequest request) {
        appointmentCreateService.createAppointment(memberId, groupId, request);
    }

    public AppointmentRecommendedOptionsResponse getRecommendedAppointmentOptions(Long memberId, Long appointmentId) {
        return appointmentRecommendedOptionService.getRecommendedAppointmentOptions(memberId, appointmentId);
    }
}
