package org.noostak.appointment.domain;

import org.noostak.appointment.domain.vo.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query(nativeQuery = true, value =
            "SELECT * " +
                    "FROM appointment " +
                    "WHERE appointment_status = :status "+
                    "AND groups_id = :groupId")
    List<Appointment> findAllByGroupIdConfirmed(String status, Long groupId);


    default List<Appointment> getAllByGroupIdConfirmed(AppointmentStatus status, Long groupId){
        return findAllByGroupIdConfirmed(status.name(),groupId);
    }

}
