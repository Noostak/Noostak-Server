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
    private Long appointOptionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "appointment_option_status")
    private AppointmentOptionStatus appointmentOptionStatus;

    @Column
    private LocalDateTime appointmentOptionDate;

    @Column
    private LocalDateTime appointmentOptionStartTime;

    @Column
    private LocalDateTime appointmentOptionEndTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;
}
