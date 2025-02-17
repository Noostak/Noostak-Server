package org.noostak.appointment.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.noostak.global.entity.BaseTimeEntity;

import java.time.LocalDateTime;

@Entity
@Getter
@RequiredArgsConstructor
public class AppointmentHostSelectionTimes extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentHostSelectionTimeId;

    @Column(name = "appointment_host_selection_date")
    private LocalDateTime date;

    @Column(name = "appointment_host_selection_start_time")
    private LocalDateTime startTime;

    @Column(name = "appointment_host_selection_end_time")
    private LocalDateTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    private AppointmentHostSelectionTimes(final LocalDateTime date, final LocalDateTime startTime, final LocalDateTime endTime) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static AppointmentHostSelectionTimes of(final LocalDateTime date, final LocalDateTime startTime, final LocalDateTime endTime) {
        return new AppointmentHostSelectionTimes(date, startTime, endTime);
    }
}
