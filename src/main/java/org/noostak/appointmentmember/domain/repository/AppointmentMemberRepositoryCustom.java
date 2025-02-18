package org.noostak.appointmentmember.domain.repository;

import org.noostak.appointmentmember.domain.AppointmentMember;

import java.util.Optional;

public interface AppointmentMemberRepositoryCustom {
    Optional<AppointmentMember> findByMemberIdAndAppointmentId(Long memberId, Long appointmentId);
}
