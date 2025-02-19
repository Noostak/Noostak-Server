package org.noostak.appointmentmember.domain.repository;

import org.noostak.appointmentmember.domain.AppointmentMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentMemberRepository extends JpaRepository<AppointmentMember, Long>, AppointmentMemberRepositoryCustom {
}
