package org.noostak.appointmentmember.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.noostak.global.entity.BaseTimeEntity;
import org.noostak.member.domain.Member;

import java.time.LocalDateTime;

@Entity
@Getter
@RequiredArgsConstructor
public class AppointmentMemberAvailableTimes extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentMemberAvailableTimeId;

    @Column
    private LocalDateTime appointmentMemberAvailableDate;

    @Column
    private LocalDateTime appointmentMemberAvailableStartTime;

    @Column
    private LocalDateTime appointmentMemberAvailableEndTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_member_id")
    private AppointmentMember appointmentMember;

}
