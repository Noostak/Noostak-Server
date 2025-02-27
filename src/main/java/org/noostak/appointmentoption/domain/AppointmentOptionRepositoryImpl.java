package org.noostak.appointmentoption.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.noostak.appointmentoption.domain.vo.AppointmentOptionStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.noostak.appointmentoption.domain.QAppointmentOption.appointmentOption;

@RequiredArgsConstructor
public class AppointmentOptionRepositoryImpl implements AppointmentOptionRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<AppointmentOption> findByAppointmentConfirmedYearAndMonth(Long appointmentId, int year, int month) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(appointmentOption)
                        .where(
                                appointmentOption.appointment.id.eq(appointmentId),
                                appointmentOption.status.eq(AppointmentOptionStatus.CONFIRMED),
                                appointmentOption.date.year().eq(year),
                                appointmentOption.date.month().eq(month)
                        )
                        .limit(1)
                        .fetchOne()
        );
    }

    @Override
    public Optional<AppointmentOption> findByAppointmentConfirmedBetweenDate(Long appointmentId, LocalDate startDate, LocalDate endDate) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(appointmentOption)
                        .where(
                                appointmentOption.appointment.id.eq(appointmentId),
                                appointmentOption.status.eq(AppointmentOptionStatus.CONFIRMED),
                                appointmentOption.date.between(
                                        startDate.atStartOfDay(),
                                        endDate.atTime(23, 59, 59)
                                )
                        )
                        .limit(1)
                        .fetchOne()
        );
    }
}
