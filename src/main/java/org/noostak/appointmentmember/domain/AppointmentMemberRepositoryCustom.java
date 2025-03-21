package org.noostak.appointmentmember.domain;

import org.noostak.appointmentmember.domain.vo.AppointmentAvailability;

import java.util.List;
import java.util.Optional;

public interface AppointmentMemberRepositoryCustom {
    Optional<AppointmentMember> findByMemberIdAndAppointmentId(Long memberId, Long appointmentId);
    List<AppointmentMember> findAllWithAvailableTimes(Long appointmentId);
    List<AppointmentMember> findByAppointmentId(Long appointmentId);
    Optional<AppointmentMember> findByMemberIdAndAppointmentIdAndAppointmentAvailability
            (Long appointmentId, Long memberId, AppointmentAvailability appointmentAvailability);
}
