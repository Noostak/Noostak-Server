package org.noostak.appointmentoption.domain;

import java.time.LocalDate;
import java.util.Optional;

public interface AppointmentOptionRepositoryCustom {
    Optional<AppointmentOption> findByAppointmentConfirmedYearAndMonth(Long appointmentId, int year, int month);
    Optional<AppointmentOption> findByAppointmentConfirmedBetweenDate(Long appointmentId, LocalDate startDate, LocalDate endDate);
    Optional<AppointmentOption> findConfirmedOptionByAppointmentId(Long appointmentId);
}

