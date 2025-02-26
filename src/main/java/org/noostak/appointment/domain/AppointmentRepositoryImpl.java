package org.noostak.appointment.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;


import static org.noostak.appointment.domain.QAppointment.appointment;

@RequiredArgsConstructor
public class AppointmentRepositoryImpl implements AppointmentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Appointment> findAllByGroupId(Long groupId) {
        return queryFactory
                .selectFrom(appointment)
                .where(appointment.group.id.eq(groupId))
                .orderBy(appointment.createdAt.desc())
                .fetch();
    }
}
