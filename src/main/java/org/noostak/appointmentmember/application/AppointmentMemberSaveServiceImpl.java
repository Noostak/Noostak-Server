package org.noostak.appointmentmember.application;

import lombok.RequiredArgsConstructor;
import org.noostak.appointment.domain.Appointment;
import org.noostak.appointmentmember.domain.AppointmentMember;
import org.noostak.appointmentmember.domain.AppointmentMemberRepository;
import org.noostak.appointmentmember.domain.vo.AppointmentAvailability;
import org.noostak.group.domain.Group;
import org.noostak.member.domain.Member;
import org.noostak.membergroup.domain.MemberGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AppointmentMemberSaveServiceImpl implements AppointmentMemberSaveService{
    private final MemberGroupRepository memberGroupRepository;
    private final AppointmentMemberRepository appointmentMemberRepository;

    @Override
    @Transactional
    public void saveAppointmentMemberByGroup(Group group, Appointment appointment) {
        memberGroupRepository.findMembersByGroupId(group.getId())
                .forEach(member -> saveAppointmentMember(appointment, member));
    }

    @Override
    @Transactional
    public void saveAppointmentMember(Appointment appointment, Member member) {
        AppointmentMember newAppointmentMember = AppointmentMember.of(
                AppointmentAvailability.NOT_SELECTED,
                appointment,
                member);

        appointmentMemberRepository.save(newAppointmentMember);
    }
}
