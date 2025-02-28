package org.noostak.appointmentmember.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentMemberRepository extends JpaRepository<AppointmentMember, Long>, AppointmentMemberRepositoryCustom {
    List<AppointmentMember> findByAppointmentId(Long appointmentId);
}
