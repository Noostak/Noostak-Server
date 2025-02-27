package org.noostak.appointment.domain;

import org.noostak.appointment.domain.vo.AppointmentStatus;

import java.util.List;

public interface AppointmentRepositoryCustom {
    List<Appointment> findAllByGroupId(Long groupId);
    List<Appointment> findAllByGroupIdConfirmed(AppointmentStatus status, Long groupId);
    List<Appointment> findAllByGroupIdConfirmed(Long groupId);
}
