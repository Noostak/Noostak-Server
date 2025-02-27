package org.noostak.appointmentoption.application;

import org.noostak.appointmentoption.dto.response.confirmed.AppointmentConfirmedOptionResponse;

public interface AppointmentOptionConfirmedOptionService {
    AppointmentConfirmedOptionResponse getConfirmedAppointmentOption(Long memberId, Long appointmentOptionId);
}
