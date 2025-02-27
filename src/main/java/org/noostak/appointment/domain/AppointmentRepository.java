package org.noostak.appointment.domain;

import org.noostak.appointment.common.exception.AppointmentErrorCode;
import org.noostak.appointment.common.exception.AppointmentException;
import org.noostak.appointment.domain.vo.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long>, AppointmentRepositoryCustom {

    @Query(nativeQuery = true, value =
            "SELECT * " +
                    "FROM appointment " +
                    "WHERE appointment_status = :status "+
                    "AND groups_id = :groupId")
    List<Appointment> findAllByGroupIdConfirmed(String status, Long groupId);


    default List<Appointment> getAllByGroupIdConfirmed(AppointmentStatus status, Long groupId){
        return findAllByGroupIdConfirmed(status.name(),groupId);
    }

    default Appointment getById(Long appointmentId){
        return findById(appointmentId)
                .orElseThrow(()-> new AppointmentException(AppointmentErrorCode.APPOINTMENT_NOT_FOUND));
    }
}
