package org.noostak.appointmentmember.domain.repository;

import org.noostak.appointmentmember.domain.AppointmentMember;
import org.noostak.appointmentmember.domain.AppointmentMemberAvailableTimes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentMemberAvailableTimesRepository extends JpaRepository<AppointmentMemberAvailableTimes, Long> {

    List<AppointmentMemberAvailableTimes> findByAppointmentMember(AppointmentMember appointmentMember);

    void deleteByAppointmentMember(AppointmentMember appointmentMember);
}
