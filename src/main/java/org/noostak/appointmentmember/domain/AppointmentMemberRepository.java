package org.noostak.appointmentmember.domain;

import org.noostak.appointment.common.exception.AppointmentErrorCode;
import org.noostak.appointmentmember.common.exception.AppointmentMemberException;
import org.noostak.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentMemberRepository extends JpaRepository<AppointmentMember, Long>, AppointmentMemberRepositoryCustom {
    List<AppointmentMember> findByAppointmentId(Long appointmentId);

    List<AppointmentMember> findByMember(Member member);

    default AppointmentMember getByMemberIdAndAppointmentId(Long memberId, Long appointmentId){
        return findByMemberIdAndAppointmentId(memberId,appointmentId).
                orElseThrow(()->new AppointmentMemberException(AppointmentErrorCode.APPOINTMENT_NOT_FOUND));
    }
}
