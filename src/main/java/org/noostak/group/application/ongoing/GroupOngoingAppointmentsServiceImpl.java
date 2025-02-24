package org.noostak.group.application.ongoing;

import lombok.RequiredArgsConstructor;
import org.noostak.appointment.domain.Appointment;
import org.noostak.appointment.domain.AppointmentRepository;
import org.noostak.group.common.exception.GroupErrorCode;
import org.noostak.group.common.exception.GroupException;
import org.noostak.group.domain.Group;
import org.noostak.group.domain.GroupRepository;
import org.noostak.group.dto.response.ongoing.GroupOngoingAppointmentsResponse;
import org.noostak.group.dto.response.ongoing.GroupOngoingInfoResponse;
import org.noostak.group.dto.response.ongoing.OngoingAppointmentResponse;
import org.noostak.infra.S3Service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupOngoingAppointmentsServiceImpl implements GroupOngoingAppointmentsService {

    private final GroupRepository groupRepository;
    private final AppointmentRepository appointmentRepository;
    private final S3Service s3Service;

    @Override
    public GroupOngoingAppointmentsResponse getGroupOngoingAppointments(Long groupId) {
        Group group = getGroup(groupId);

        GroupOngoingInfoResponse groupInfo = convertToGroupOngoingInfo(group);

        List<OngoingAppointmentResponse> ongoingAppointments = getOngoingAppointments(group);

        return GroupOngoingAppointmentsResponse.of(groupInfo, ongoingAppointments);
    }

    private Group getGroup(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupException(GroupErrorCode.GROUP_NOT_FOUND));
    }

    private GroupOngoingInfoResponse convertToGroupOngoingInfo(Group group) {
        String groupProfileImageUrl = s3Service.getImageUrl(group.getKey().value());

        return GroupOngoingInfoResponse.of(
                group.getName().value(),
                groupProfileImageUrl,
                group.getCount().value(),
                group.getCode().value()
        );
    }

    private List<OngoingAppointmentResponse> getOngoingAppointments(Group group) {
        List<Appointment> ongoingAppointments = appointmentRepository.findOngoingAppointmentsByGroup(group);

        return ongoingAppointments.stream()
                .map(appointment -> OngoingAppointmentResponse.of(
                        appointment.getId(),
                        appointment.getName().value(),
                        appointment.getMemberCount().value()
                ))
                .toList();
    }
}
