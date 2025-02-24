package org.noostak.appointment.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentHostSelectionTimeRepository extends JpaRepository<AppointmentHostSelectionTime, Long> {
    List<AppointmentHostSelectionTime> findByAppointmentId(Long appointmentId);
}
