package org.noostak.appointmentmember.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentMemberRepository extends JpaRepository<AppointmentMember, Long>, AppointmentMemberRepositoryCustom {
}
