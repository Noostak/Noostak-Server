package org.noostak.likes.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.noostak.appointmentmember.domain.AppointmentMember;
import org.noostak.appointmentoption.domain.AppointmentOption;
import org.noostak.global.entity.BaseTimeEntity;

@Entity
@Getter
@RequiredArgsConstructor
@Table(name = "likes")
public class Like extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "likes_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_member_id")
    private AppointmentMember appointmentMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_option_id")
    private AppointmentOption appointmentOption;

    private Like(AppointmentMember appointmentMember, AppointmentOption appointmentOption) {
        this.appointmentMember = appointmentMember;
        this.appointmentOption = appointmentOption;
    }

    public static Like of(AppointmentMember appointmentMember, AppointmentOption option) {
        return new Like(appointmentMember, option);
    }
}
