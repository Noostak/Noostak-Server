package org.noostak.appointment.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.noostak.global.entity.BaseTimeEntity;

import java.time.LocalDateTime;

@Entity
@Getter
@RequiredArgsConstructor
public class AppointmentHostSelectionTime extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_host_selection_time_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @Column(name = "appointment_host_selection_date")
    private LocalDateTime date;

    @Column(name = "appointment_host_selection_start_time")
    private LocalDateTime startTime;

    @Column(name = "appointment_host_selection_end_time")
    private LocalDateTime endTime;

    private AppointmentHostSelectionTime(final Appointment appointment, final LocalDateTime date, final LocalDateTime startTime, final LocalDateTime endTime) {
        this.appointment = appointment;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static AppointmentHostSelectionTime of(Appointment appointment, LocalDateTime date, LocalDateTime startTime, LocalDateTime endTime) {
        return new AppointmentHostSelectionTime(appointment, date, startTime, endTime);
    }
}
