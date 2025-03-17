package org.noostak.appointmentmember.application;

import org.noostak.appointment.domain.Appointment;
import org.noostak.group.domain.Group;
import org.noostak.member.domain.Member;

public interface AppointmentMemberSaveService {
    void saveAppointmentMemberByGroup(Group group, Appointment appointment);

    void saveAppointmentMember(Appointment appointment, Member member);
}
