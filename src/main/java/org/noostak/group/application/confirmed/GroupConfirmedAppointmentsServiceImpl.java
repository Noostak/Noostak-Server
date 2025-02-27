package org.noostak.group.application.confirmed;

import lombok.RequiredArgsConstructor;
import org.noostak.appointment.domain.Appointment;
import org.noostak.appointment.domain.AppointmentRepository;
import org.noostak.appointmentoption.domain.AppointmentOption;
import org.noostak.appointmentoption.domain.AppointmentOptionRepository;
import org.noostak.group.common.exception.GroupErrorCode;
import org.noostak.group.common.exception.GroupException;
import org.noostak.group.domain.Group;
import org.noostak.group.domain.GroupRepository;
import org.noostak.group.dto.response.confirmed.*;
import org.noostak.group.util.GroupConfirmedResponseMapper;
import org.noostak.infra.S3Service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupConfirmedAppointmentsServiceImpl implements GroupConfirmedAppointmentsService {

    private final GroupRepository groupRepository;
    private final AppointmentRepository appointmentRepository;
    private final AppointmentOptionRepository appointmentOptionRepository;
    private final S3Service s3Service;

    @Override
    public GroupConfirmedAppointmentsResponse getGroupConfirmedAppointments(Long memberId, Long groupId) {
        Group group = getGroup(groupId);
        String groupProfileImageUrl = getGroupProfileImageUrl(group);
        GroupConfirmedInfoResponse groupInfoResponse = GroupConfirmedResponseMapper.toGroupConfirmedInfo(group, groupProfileImageUrl);

        List<Appointment> confirmedAppointments = getConfirmedAppointments(groupId);
        List<AppointmentOption> confirmedOptions = getConfirmedAppointmentOptions(confirmedAppointments);

        List<ConfirmedAppointmentsResponse> confirmedAppointmentsResponse =
                GroupConfirmedResponseMapper.toConfirmedAppointmentsResponse(confirmedAppointments, confirmedOptions);

        return GroupConfirmedAppointmentsResponse.of(groupInfoResponse, confirmedAppointmentsResponse);
    }

    private Group getGroup(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupException(GroupErrorCode.GROUP_NOT_FOUND));
    }

    private String getGroupProfileImageUrl(Group group) {
        return s3Service.getImageUrl(group.getKey().value());
    }

    private List<Appointment> getConfirmedAppointments(Long groupId) {
        return appointmentRepository.findAllByGroupIdConfirmed(groupId);
    }

    private List<AppointmentOption> getConfirmedAppointmentOptions(List<Appointment> confirmedAppointments) {
        return confirmedAppointments.stream()
                .map(appointment -> appointmentOptionRepository.findConfirmedOptionByAppointmentId(appointment.getId()))
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }
}
