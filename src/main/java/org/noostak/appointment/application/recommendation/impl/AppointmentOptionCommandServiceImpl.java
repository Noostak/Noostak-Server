package org.noostak.appointment.application.recommendation.impl;

import lombok.RequiredArgsConstructor;
import org.noostak.appointment.application.recommendation.AppointmentOptionCommandService;
import org.noostak.appointment.domain.Appointment;
import org.noostak.appointment.dto.response.recommendation.AppointmentOptionAvailabilityResponse;
import org.noostak.appointmentoption.domain.AppointmentOption;
import org.noostak.appointmentoption.domain.AppointmentOptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentOptionCommandServiceImpl implements AppointmentOptionCommandService {
    private final AppointmentOptionRepository appointmentOptionRepository;

    @Override
    @Transactional
    public List<AppointmentOption> saveOptions(Appointment appointment, List<AppointmentOptionAvailabilityResponse> options) {
        return options.stream()
                .map(dto -> appointmentOptionRepository.save(
                        AppointmentOption.of(appointment, dto.date(), dto.startTime(), dto.endTime())))
                .toList();
    }
}
