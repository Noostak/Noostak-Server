package org.noostak.appointment.application.recommendation;

import java.util.Map;

public interface AppointmentParticipantQueryService {
    Map<Long, String> findParticipantNamesByAppointmentId(Long appointmentId);
}
