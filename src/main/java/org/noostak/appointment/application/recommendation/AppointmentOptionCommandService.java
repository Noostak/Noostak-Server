package org.noostak.appointment.application.recommendation;

import org.noostak.appointment.domain.Appointment;
import org.noostak.appointment.dto.response.recommendation.AppointmentOptionAvailabilityResponse;
import org.noostak.appointmentoption.domain.AppointmentOption;

import java.util.List;

public interface AppointmentOptionCommandService {
    List<AppointmentOption> saveOptions(Appointment appointment, List<AppointmentOptionAvailabilityResponse> options);
}
