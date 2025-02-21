package org.noostak.appointmentmember.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.noostak.global.entity.BaseTimeEntity;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AppointmentMemberAvailableTime extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "appointment_member_available_times_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_member_id")
    private AppointmentMember appointmentMember;

    @Column(name = "appointment_member_available_date")
    private LocalDateTime date;

    @Column(name = "appointment_member_available_start_time")
    private LocalDateTime startTime;

    @Column(name = "appointment_member_available_end_time")
    private LocalDateTime endTime;

    private AppointmentMemberAvailableTime(final AppointmentMember appointmentMember, final LocalDateTime date, final LocalDateTime startTime, final LocalDateTime endTime) {
        this.appointmentMember = appointmentMember;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static AppointmentMemberAvailableTime of(final AppointmentMember appointmentMember, final LocalDateTime date, final LocalDateTime startTime, final LocalDateTime endTime) {
        return new AppointmentMemberAvailableTime(appointmentMember, date, startTime, endTime);
    }
}

