package org.noostak.appointmentmember.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentMemberAvailableTimesRepository extends JpaRepository<AppointmentMemberAvailableTime, Long>{

    List<AppointmentMemberAvailableTime> findByAppointmentMember(AppointmentMember appointmentMember);

    void deleteByAppointmentMember(AppointmentMember appointmentMember);
}
