package org.noostak.appointment.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.noostak.appointment.domain.vo.AppointmentStatus;
import org.noostak.group.domain.Group;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.noostak.appointment.domain.QAppointment.appointment;

@Repository
@RequiredArgsConstructor
public class AppointmentRepositoryCustomImpl implements AppointmentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Appointment> findOngoingAppointmentsByGroup(Group group) {
        return queryFactory
                .selectFrom(appointment)
                .where(
                        appointment.group.eq(group),
                        appointment.appointmentStatus.eq(AppointmentStatus.PROGRESS)
                )
                .fetch();
    }
}
