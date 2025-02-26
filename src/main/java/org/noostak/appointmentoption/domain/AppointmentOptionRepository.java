package org.noostak.appointmentoption.domain;

import org.noostak.appointment.domain.Appointment;
import org.noostak.appointmentoption.domain.vo.AppointmentOptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentOptionRepository extends JpaRepository<AppointmentOption, Long> {

    @Query(nativeQuery = true,
            value = "SELECT * " +
                    "FROM appointment_option " +
                    "WHERE appointment_id = :appointmentId " +
                    "AND appointment_option_status = :status " +
                    "AND YEAR(appointment_option_date) = :year " +
                    "AND MONTH(appointment_option_date) = :month " +
                    "LIMIT 1")
    AppointmentOption findByAppointmentConfirmedYearAndMonth(
            Long appointmentId,
            String status,
            int year,
            int month);

    default AppointmentOption getByAppointmentConfirmedYearAndMonth(Appointment appointment, int year, int month){
        return findByAppointmentConfirmedYearAndMonth(
                appointment.getId(),
                AppointmentOptionStatus.CONFIRMED.name(),
                year,
                month
        );
    }

    @Query(nativeQuery = true,
            value = "SELECT * " +
                    "FROM appointment_option " +
                    "WHERE appointment_id = :appointmentId " +
                    "AND appointment_option_status = :status " +
                    "AND appointment_option_date BETWEEN :startDate AND :endDate " +
                    "LIMIT 1")
    AppointmentOption findAllByAppointmentConfirmedBetweenDate(Long appointmentId,
                                                 String status,
                                                 LocalDate startDate,
                                                 LocalDate endDate);


    default AppointmentOption getAllByAppointmentConfirmedBetweenDate(
            Appointment appointment,
            LocalDate startDate,
            LocalDate endDate
    ){
        return findAllByAppointmentConfirmedBetweenDate(
                appointment.getId(),
                AppointmentOptionStatus.CONFIRMED.name(),
                startDate,
                endDate
        );
    }
}
