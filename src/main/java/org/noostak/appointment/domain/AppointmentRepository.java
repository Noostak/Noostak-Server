package org.noostak.appointment.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Long>, AppointmentRepositoryCustom {
}
