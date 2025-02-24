package org.noostak.appointmentoption.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppointmentOptionService {

    private final AppointmentOptionConfirmService confirmAppointment;

    public void confirmAppointment(Long appointmentOptionId) {
        confirmAppointment.confirmAppointment(appointmentOptionId);
    }
}
