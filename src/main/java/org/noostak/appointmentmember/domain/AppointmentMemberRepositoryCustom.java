package org.noostak.appointmentmember.domain;

import java.util.List;
import java.util.Optional;

public interface AppointmentMemberRepositoryCustom {
    Optional<AppointmentMember> findByMemberIdAndAppointmentId(Long memberId, Long appointmentId);
    List<AppointmentMember> findAllWithAvailableTimes(Long appointmentId);
    List<AppointmentMember> findByAppointmentId(Long appointmentId);
}
