package org.noostak.appointmentoption.domain;

import org.noostak.appointment.domain.Appointment;
import org.noostak.appointmentoption.common.exception.AppointmentOptionErrorCode;
import org.noostak.appointmentoption.common.exception.AppointmentOptionException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface AppointmentOptionRepository extends JpaRepository<AppointmentOption, Long>, AppointmentOptionRepositoryCustom {

    default AppointmentOption getByAppointmentOptionId(Long appointmentOptionId){
        return findById(appointmentOptionId)
                .orElseThrow(() -> new AppointmentOptionException(AppointmentOptionErrorCode.APPOINTMENT_OPTION_NOT_FOUND));
    }

    Optional<AppointmentOption> findFirstByAppointmentAndStartTimeAndEndTime
            (Appointment appointment, LocalDateTime startTime, LocalDateTime endTime);

    default AppointmentOption getByAppointmentAndTimes(Appointment appointment, AppointmentOption option){
        return findFirstByAppointmentAndStartTimeAndEndTime(appointment,option.getStartTime(),option.getEndTime())
                .orElse(null);
    }
}
