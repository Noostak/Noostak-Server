package org.noostak.appointmentoption.application;

import lombok.RequiredArgsConstructor;
import org.noostak.appointmentoption.common.exception.AppointmentOptionErrorCode;
import org.noostak.appointmentoption.common.exception.AppointmentOptionException;
import org.noostak.appointmentoption.domain.AppointmentOption;
import org.noostak.appointmentoption.domain.AppointmentOptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AppointmentOptionConfirmServiceImpl implements AppointmentOptionConfirmService {

    private final AppointmentOptionRepository appointmentOptionRepository;

    @Override
    @Transactional
    public void confirmAppointment(Long appointmentOptionId) {
        AppointmentOption appointmentOption = appointmentOptionRepository.findById(appointmentOptionId)
                .orElseThrow(() -> new AppointmentOptionException(AppointmentOptionErrorCode.APPOINTMENT_OPTION_NOT_FOUND));

        appointmentOption.confirm();
    }
}
