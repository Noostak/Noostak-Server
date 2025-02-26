package org.noostak.appointment.application.recommendation;

import lombok.RequiredArgsConstructor;
import org.noostak.appointment.common.exception.AppointmentErrorCode;
import org.noostak.appointment.common.exception.AppointmentException;
import org.noostak.appointment.domain.Appointment;
import org.noostak.appointment.domain.AppointmentRepository;
import org.noostak.appointment.dto.response.AppointmentPriorityGroupResponse;
import org.noostak.appointment.dto.response.AppointmentRecommendedOptionsResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AppointmentRecommendedOptionServiceImpl implements AppointmentRecommendedOptionService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentRecommendationFacade recommendationFacade;

    @Override
    @Transactional
    public AppointmentRecommendedOptionsResponse getRecommendedAppointmentOptions(Long memberId, Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentException(AppointmentErrorCode.APPOINTMENT_NOT_FOUND));

        boolean isAppointmentHost = appointment.getAppointmentHostId().equals(memberId);

        List<AppointmentPriorityGroupResponse> priorityGroups = recommendationFacade.getRecommendedOptions(memberId, appointmentId, appointment);

        return AppointmentRecommendedOptionsResponse.of(isAppointmentHost, priorityGroups);
    }
}
