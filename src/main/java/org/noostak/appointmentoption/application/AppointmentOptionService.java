package org.noostak.appointmentoption.application;

import lombok.RequiredArgsConstructor;
import org.noostak.appointmentoption.dto.response.confirmed.AppointmentConfirmedOptionResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppointmentOptionService {

    private final AppointmentOptionConfirmService confirmAppointment;
    private final AppointmentOptionConfirmedOptionService confirmedOptionService;

    public void confirmAppointment(Long appointmentOptionId) {
        confirmAppointment.confirmAppointment(appointmentOptionId);
    }

    public AppointmentConfirmedOptionResponse getConfirmedAppointmentOption(Long memberId, Long appointmentOptionId) {
        return confirmedOptionService.getConfirmedAppointmentOption(memberId, appointmentOptionId);
    }
}
