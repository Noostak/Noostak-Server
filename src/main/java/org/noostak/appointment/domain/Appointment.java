package org.noostak.appointment.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.noostak.appointment.common.exception.AppointmentErrorCode;
import org.noostak.appointment.common.exception.AppointmentException;
import org.noostak.appointment.domain.vo.*;
import org.noostak.global.entity.BaseTimeEntity;
import org.noostak.group.domain.Group;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Appointment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "groups_id")
    private Group group;

    @Column(name = "appointment_host_id")
    private Long appointmentHostId;

    @Embedded
    @AttributeOverride(name = "name", column = @Column(name = "appointment_name"))
    private AppointmentName name;

    @Embedded
    @AttributeOverride(name = "count", column = @Column(name = "appointment_member_count"))
    private AppointmentMemberCount memberCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "appointment_status")
    private AppointmentStatus appointmentStatus;

    @Embedded
    @AttributeOverride(name = "duration", column = @Column(name = "appointment_duration"))
    private AppointmentDuration duration;

    @Enumerated(EnumType.STRING)
    @Column(name = "appointment_category")
    private AppointmentCategory category;

    private Appointment(final Group group, final Long appointmentHostId, final AppointmentName name, final AppointmentDuration duration,
                        final AppointmentMemberCount memberCount, final AppointmentCategory category,
                        final AppointmentStatus appointmentStatus) {
        this.group = group;
        this.appointmentHostId = appointmentHostId;
        this.name = name;
        this.duration = duration;
        this.memberCount = memberCount;
        this.category = category;
        this.appointmentStatus = appointmentStatus;
    }

    public static Appointment of(final Group group, final Long appointmentHostId, final String name, final Long duration,
                                 final String category, final AppointmentStatus appointmentStatus) {
        if (group == null) {
            throw new AppointmentException(AppointmentErrorCode.GROUP_NOT_FOUND);
        }
        return new Appointment(
                group,
                appointmentHostId,
                AppointmentName.from(name),
                AppointmentDuration.from(duration),
                AppointmentMemberCount.from(1L),
                AppointmentCategory.from(category),
                appointmentStatus
        );
    }
}


