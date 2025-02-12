package org.noostak.likes.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.noostak.appointmentmember.domain.AppointmentMember;
import org.noostak.appointmentoption.domain.AppointmentOption;
import org.noostak.likes.domain.vo.LikesCount;

@Entity
@Getter
@RequiredArgsConstructor
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;

    @Embedded
    @AttributeOverride(name = "count", column = @Column(name = "likes_count"))
    private LikesCount likesCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_member_id")
    private AppointmentMember appointmentMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_option_id")
    private AppointmentOption appointmentOption;
}
