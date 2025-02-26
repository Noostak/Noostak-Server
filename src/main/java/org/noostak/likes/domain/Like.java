package org.noostak.likes.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.noostak.appointmentmember.domain.AppointmentMember;
import org.noostak.appointmentoption.domain.AppointmentOption;
import org.noostak.global.entity.BaseTimeEntity;
import org.noostak.likes.domain.vo.LikesCount;

@Entity
@Getter
@RequiredArgsConstructor
@Table(name = "likes")
public class Like extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;

    @Embedded
    @AttributeOverride(name = "count", column = @Column(name = "likes_count"))
    private LikesCount likesCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_member_id")
    private AppointmentMember appointmentMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_option_id")
    private AppointmentOption appointmentOption;

    private Like(LikesCount likesCount, AppointmentMember appointmentMember, AppointmentOption appointmentOption) {
        this.likesCount = likesCount;
        this.appointmentMember = appointmentMember;
        this.appointmentOption = appointmentOption;
    }

    public static Like of(LikesCount likesCount, AppointmentMember appointmentMember, AppointmentOption appointmentOption) {
        return new Like(likesCount, appointmentMember, appointmentOption);
    }
}
