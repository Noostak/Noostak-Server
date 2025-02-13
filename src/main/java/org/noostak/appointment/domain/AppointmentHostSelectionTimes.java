package org.noostak.appointment.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@RequiredArgsConstructor
public class AppointmentHostSelectionTimes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentHostSelectionTimeId;

    @Column
    private LocalDateTime appointmentHostSelectionDate;

    @Column
    private LocalDateTime appointmentHostSelectionStartTime;

    @Column
    private LocalDateTime appointmentHostSelectionEndTime;

// TODO:
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "appointment_id")
//    private Appointment appointment;
}
