package org.noostak.appointment.application.recommendation;

import org.noostak.appointmentmember.domain.AppointmentMemberAvailableTime;

import java.util.List;
import java.util.Map;

public interface AppointmentMemberAvailabilityQueryService {
    Map<Long, List<AppointmentMemberAvailableTime>> findAvailableTimeSlotsByAppointmentId(Long appointmentId);
}
