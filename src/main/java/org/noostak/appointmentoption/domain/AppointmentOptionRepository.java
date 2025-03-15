package org.noostak.appointmentoption.domain;

import org.noostak.appointment.domain.Appointment;
import org.noostak.likes.common.exception.LikesErrorCode;
import org.noostak.likes.common.exception.LikesException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface AppointmentOptionRepository extends JpaRepository<AppointmentOption, Long>, AppointmentOptionRepositoryCustom {

    default AppointmentOption getByAppointmentOptionId(Long appointmentId){
        return findById(appointmentId)
                .orElseThrow(() -> new LikesException(LikesErrorCode.OPTION_NOT_FOUND));
    }

    Optional<AppointmentOption> findFirstByAppointmentAndStartTimeAndEndTime
            (Appointment appointment, LocalDateTime startTime, LocalDateTime endTime);

    default AppointmentOption getByAppointmentAndTimes(Appointment appointment, AppointmentOption option){
        return findFirstByAppointmentAndStartTimeAndEndTime(appointment,option.getStartTime(),option.getEndTime())
                .orElse(null);
    }
}
