package org.noostak.appointmentoption.application.info;

import lombok.RequiredArgsConstructor;
import org.noostak.appointmentoption.application.AppointmentOptionConfirmService;
import org.noostak.appointmentoption.application.AppointmentOptionConfirmedOptionService;
import org.noostak.appointmentoption.dto.response.info.AppointmentOptionInfoResponse;
import org.noostak.appointmentoption.dto.response.confirmed.AppointmentConfirmedOptionResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppointmentOptionService {

    private final AppointmentOptionConfirmService confirmAppointment;
    private final AppointmentOptionConfirmedOptionService confirmedOptionService;
    private final AppointmentOptionInfoService appointmentOptionInfoService;

    public void confirmAppointment(Long appointmentOptionId) {
        confirmAppointment.confirmAppointment(appointmentOptionId);
    }

    public AppointmentConfirmedOptionResponse getConfirmedAppointmentOption(Long memberId, Long appointmentOptionId) {
        return confirmedOptionService.getConfirmedAppointmentOption(memberId, appointmentOptionId);
    }

    public AppointmentOptionInfoResponse getAppointmentOptionInfo(Long memberId, Long appointmentOptionId) {
        return appointmentOptionInfoService.getAppointmentOptionInfo(memberId, appointmentOptionId);
    }
}
