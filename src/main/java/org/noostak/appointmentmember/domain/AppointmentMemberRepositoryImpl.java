package org.noostak.appointmentmember.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.noostak.appointment.domain.QAppointment;
import org.noostak.member.domain.QMember;

import java.util.List;
import java.util.Optional;

import static org.noostak.appointmentmember.domain.QAppointmentMember.appointmentMember;
import static org.noostak.appointmentmember.domain.QAppointmentMemberAvailableTime.appointmentMemberAvailableTime;

@RequiredArgsConstructor
public class AppointmentMemberRepositoryImpl implements AppointmentMemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<AppointmentMember> findByMemberIdAndAppointmentId(Long memberId, Long appointmentId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(appointmentMember)
                        .join(appointmentMember.member, QMember.member).fetchJoin()
                        .join(appointmentMember.appointment, QAppointment.appointment).fetchJoin()
                        .where(
                                appointmentMember.member.id.eq(memberId),
                                appointmentMember.appointment.id.eq(appointmentId)
                        )
                        .fetchOne()
        );
    }

    @Override
    public List<AppointmentMember> findAllWithAvailableTimes(Long appointmentId) {
        return queryFactory
                .selectFrom(appointmentMember)
                .leftJoin(appointmentMemberAvailableTime)
                .on(appointmentMemberAvailableTime.appointmentMember.eq(appointmentMember))
                .where(appointmentMember.appointment.id.eq(appointmentId))
                .distinct()
                .fetch();
    }
}
