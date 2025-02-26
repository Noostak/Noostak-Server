package org.noostak.appointment.domain;

import org.noostak.appointment.common.exception.AppointmentErrorCode;
import org.noostak.appointment.common.exception.AppointmentException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long>, AppointmentRepositoryCustom {
    default Appointment getById(Long appointmentId){
        return findById(appointmentId)
                .orElseThrow(()-> new AppointmentException(AppointmentErrorCode.APPOINTMENT_NOT_FOUND));
    }
}
