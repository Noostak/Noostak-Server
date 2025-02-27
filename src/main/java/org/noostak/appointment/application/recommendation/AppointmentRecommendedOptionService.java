package org.noostak.appointment.application.recommendation;

import org.noostak.appointment.dto.response.recommendation.AppointmentRecommendedOptionsResponse;

public interface AppointmentRecommendedOptionService {
    AppointmentRecommendedOptionsResponse getRecommendedAppointmentOptions(Long memberId, Long appointmentId);
}
