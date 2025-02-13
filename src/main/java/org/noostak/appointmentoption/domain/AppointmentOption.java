package org.noostak.appointmentoption.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.noostak.appointmentoption.domain.vo.AppointmentOptionStatus;
import org.noostak.appointmentoption.domain.vo.AppointmentOptionTimeStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@RequiredArgsConstructor
public class AppointmentOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointOptionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "appointment_option_status")
    private AppointmentOptionStatus appointmentOptionStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "appointment_option_time_status")
    private AppointmentOptionTimeStatus appointmentOptionTimeStatus;

    @Column
    private LocalDate appointmentOptionDate;

    @Column
    private LocalDateTime appointmentOptionStartTime;

    @Column
    private LocalDateTime appointmentOptionEndTime;

// TODO:
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "appointment_id")
//    private Appointment appointment;
}
