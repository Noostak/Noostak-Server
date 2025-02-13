package org.noostak.appointment.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.noostak.appointment.domain.vo.AppointmentCategory;
import org.noostak.appointment.domain.vo.AppointmentDuration;
import org.noostak.appointment.domain.vo.AppointmentMemberCount;
import org.noostak.appointment.domain.vo.AppointmentStatus;
import org.noostak.global.entity.BaseTimeEntity;
import org.noostak.group.domain.Group;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Appointment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    private Long appointmentHostId;

    @Embedded
    @AttributeOverride(name = "count", column = @Column(name = "appointment_member_count"))
    private AppointmentMemberCount memberCount;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus appointmentStatus;

    @Embedded
    @AttributeOverride(name = "duration", column = @Column(name = "appointment_duration"))
    private AppointmentDuration duration;

    @Enumerated(EnumType.STRING)
    private AppointmentCategory category;

    private Appointment(final Long appointmentHostId, final Long duration, final String category, final AppointmentStatus appointmentStatus) {
        this.appointmentHostId = appointmentHostId;
        this.duration = AppointmentDuration.from(duration);
        this.memberCount = AppointmentMemberCount.from(1L);
        this.category = AppointmentCategory.valueOf(category);
        this.appointmentStatus = appointmentStatus;
    }

    public static Appointment of(final Long appointmentHostId, final Long duration, final String category, final AppointmentStatus appointmentStatus) {
        return new Appointment(appointmentHostId, duration, category, appointmentStatus);
    }
}
