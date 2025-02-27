package org.noostak.appointmentoption.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.noostak.appointment.domain.Appointment;
import org.noostak.appointmentoption.domain.vo.AppointmentOptionStatus;
import org.noostak.global.entity.BaseTimeEntity;

import java.time.LocalDateTime;

@Entity
@Getter
@RequiredArgsConstructor
public class AppointmentOption extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_option_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "appointment_option_status")
    private AppointmentOptionStatus status;

    @Column
    private LocalDateTime date;

    @Column
    private LocalDateTime startTime;

    @Column
    private LocalDateTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    private AppointmentOption(final Appointment appointment, final LocalDateTime date, final LocalDateTime startTime, final LocalDateTime endTime, final AppointmentOptionStatus status) {
        this.appointment = appointment;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    public static AppointmentOption of(final Appointment appointment, final LocalDateTime date, final LocalDateTime startTime, final LocalDateTime endTime) {
        return new AppointmentOption(appointment, date, startTime, endTime, AppointmentOptionStatus.UNCONFIRMED);
    }

    public void confirm() {
        this.status = AppointmentOptionStatus.CONFIRMED;
    }

    public int getDayOfMonth(){
        return date.getDayOfMonth();
    }
}
