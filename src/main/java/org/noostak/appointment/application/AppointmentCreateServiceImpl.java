package org.noostak.appointment.application;

import lombok.RequiredArgsConstructor;
import org.noostak.appointment.common.exception.AppointmentErrorCode;
import org.noostak.appointment.common.exception.AppointmentException;
import org.noostak.appointment.domain.Appointment;
import org.noostak.appointment.domain.AppointmentRepository;
import org.noostak.appointment.domain.vo.AppointmentStatus;
import org.noostak.appointment.dto.request.AppointmentCreateRequest;
import org.noostak.group.domain.Group;
import org.noostak.group.domain.GroupRepository;
import org.noostak.membergroup.domain.MemberGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AppointmentCreateServiceImpl implements AppointmentCreateService {

    private final MemberGroupRepository memberGroupRepository;
    private final GroupRepository groupRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    @Transactional
    public void createAppointment(Long memberId, Long groupId, AppointmentCreateRequest request) {
        verifyMemberIsInGroup(memberId, groupId);
        Group group = findGroupById(groupId);
        Appointment appointment = createAppointment(group, memberId, request);
        saveAppointment(appointment);
    }

    private void verifyMemberIsInGroup(Long memberId, Long groupId) {
        boolean isMemberOfGroup = memberGroupRepository.existsByMemberIdAndGroupId(memberId, groupId);
        if (!isMemberOfGroup) {
            throw new AppointmentException(AppointmentErrorCode.MEMBER_NOT_IN_GROUP);
        }
    }

    private Group findGroupById(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new AppointmentException(AppointmentErrorCode.GROUP_NOT_FOUND));
    }

    private Appointment createAppointment(Group group, Long hostId, AppointmentCreateRequest request) {
        return Appointment.of(
                group,
                hostId,
                request.appointmentName(),
                request.duration(),
                request.category(),
                AppointmentStatus.PROGRESS
        );
    }

    private void saveAppointment(Appointment appointment) {
        appointmentRepository.save(appointment);
    }
}
