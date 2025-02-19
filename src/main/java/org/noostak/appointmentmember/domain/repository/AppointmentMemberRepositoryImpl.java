package org.noostak.appointmentmember.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.noostak.appointment.domain.QAppointment;
import org.noostak.appointmentmember.domain.AppointmentMember;
import org.noostak.appointmentmember.domain.QAppointmentMember;
import org.noostak.member.domain.QMember;

import java.util.Optional;

@RequiredArgsConstructor
public class AppointmentMemberRepositoryImpl implements AppointmentMemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private final QAppointmentMember qAppointmentMember = QAppointmentMember.appointmentMember;
    private final QMember qMember = QMember.member;
    private final QAppointment qAppointment = QAppointment.appointment;

    @Override
    public Optional<AppointmentMember> findByMemberIdAndAppointmentId(Long memberId, Long appointmentId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(qAppointmentMember)
                        .join(qAppointmentMember.member, qMember).fetchJoin()
                        .join(qAppointmentMember.appointment, qAppointment).fetchJoin()
                        .where(
                                qMember.id.eq(memberId),
                                qAppointment.id.eq(appointmentId)
                        )
                        .fetchOne()
        );
    }
}
