package org.noostak.appointment.domain;

import org.noostak.appointment.domain.vo.AppointmentStatus;
import org.noostak.group.domain.Group;

import java.util.List;

public interface AppointmentRepositoryCustom {
    List<Appointment> findOngoingAppointmentsByGroup(Group group);
}
