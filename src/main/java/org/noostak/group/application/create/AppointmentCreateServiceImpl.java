package org.noostak.group.application.create;

import lombok.RequiredArgsConstructor;
import org.noostak.appointment.domain.Appointment;
import org.noostak.appointment.domain.AppointmentHostSelectionTime;
import org.noostak.appointment.domain.AppointmentHostSelectionTimeRepository;
import org.noostak.appointment.domain.AppointmentRepository;
import org.noostak.appointment.domain.vo.AppointmentStatus;
import org.noostak.group.common.exception.GroupErrorCode;
import org.noostak.group.common.exception.GroupException;
import org.noostak.group.dto.request.AppointmentCreateRequest;
import org.noostak.group.domain.Group;
import org.noostak.group.domain.GroupRepository;
import org.noostak.membergroup.domain.MemberGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AppointmentCreateServiceImpl implements AppointmentCreateService {

    private final MemberGroupRepository memberGroupRepository;
    private final GroupRepository groupRepository;
    private final AppointmentRepository appointmentRepository;
    private final AppointmentHostSelectionTimeRepository appointmentHostSelectionTimeRepository;

    @Override
    @Transactional
    public void createAppointment(Long memberId, Long groupId, AppointmentCreateRequest request) {
        verifyMemberIsInGroup(memberId, groupId);
        Group group = findGroupById(groupId);
        Appointment appointment = createAppointment(group, memberId, request);
        saveAppointment(appointment);
        saveAppointmentHostSelectionTimes(appointment, request);
    }

    private void verifyMemberIsInGroup(Long memberId, Long groupId) {
        boolean isMemberOfGroup = memberGroupRepository.existsByMemberIdAndGroupId(memberId, groupId);
        if (!isMemberOfGroup) {
            throw new GroupException(GroupErrorCode.MEMBER_NOT_IN_GROUP);
        }
    }

    private Group findGroupById(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupException(GroupErrorCode.GROUP_NOT_FOUND));
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

    private void saveAppointmentHostSelectionTimes(Appointment appointment, AppointmentCreateRequest request) {
        List<AppointmentHostSelectionTime> selectionTimes = request.appointmentHostSelectionTimes().stream()
                .map(timeRequest -> AppointmentHostSelectionTime.of(appointment, timeRequest.date(), timeRequest.startTime(), timeRequest.endTime()))
                .toList();

        appointmentHostSelectionTimeRepository.saveAll(selectionTimes);
    }
}
