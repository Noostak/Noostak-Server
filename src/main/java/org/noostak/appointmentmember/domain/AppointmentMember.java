package org.noostak.appointmentmember.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.noostak.appointment.domain.Appointment;
import org.noostak.appointmentmember.domain.vo.AppointmentAvailability;
import org.noostak.global.entity.BaseTimeEntity;
import org.noostak.member.domain.Member;

@Entity
@Getter
@RequiredArgsConstructor
public class AppointmentMember extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentMemberId;

    @Enumerated(EnumType.STRING)
    @Column(name = "availability_availability")
    private AppointmentAvailability appointmentAvailability;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
