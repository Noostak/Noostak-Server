package org.noostak.appointment.domain;

import java.util.List;

public interface AppointmentRepositoryCustom {
    List<Appointment> findAllByGroupId(Long groupId);
}
