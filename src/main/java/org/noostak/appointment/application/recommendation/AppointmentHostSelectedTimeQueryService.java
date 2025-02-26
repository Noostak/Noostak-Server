package org.noostak.appointment.application.recommendation;

import org.noostak.appointment.util.TimeSlot;

import java.util.List;

public interface AppointmentHostSelectedTimeQueryService {
    List<TimeSlot> splitHostSelectedTimeSlots(Long appointmentId, Long durationInMinutes);
}
