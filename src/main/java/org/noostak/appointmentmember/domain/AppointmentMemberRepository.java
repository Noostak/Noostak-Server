package org.noostak.appointmentmember.domain;

import org.noostak.appointment.common.exception.AppointmentErrorCode;
import org.noostak.appointmentmember.common.exception.AppointmentMemberException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AppointmentMemberRepository extends JpaRepository<AppointmentMember, Long>, AppointmentMemberRepositoryCustom {
    List<AppointmentMember> findByAppointmentId(Long appointmentId);

    default AppointmentMember getByMemberIdAndAppointmentId(Long memberId, Long appointmentId){
        return findByMemberIdAndAppointmentId(memberId,appointmentId).
                orElseThrow(()->new AppointmentMemberException(AppointmentErrorCode.APPOINTMENT_NOT_FOUND));
    }
}
