package org.noostak.group.application.ongoing;

import lombok.RequiredArgsConstructor;
import org.noostak.appointment.application.recommendation.AppointmentRecommendationFacade;
import org.noostak.appointment.common.exception.AppointmentErrorCode;
import org.noostak.appointment.common.exception.AppointmentException;
import org.noostak.appointment.domain.Appointment;
import org.noostak.appointment.domain.AppointmentRepository;
import org.noostak.appointment.dto.response.recommendation.AppointmentOptionResponse;
import org.noostak.appointment.dto.response.recommendation.AppointmentOptionTimeResponse;
import org.noostak.group.domain.Group;
import org.noostak.group.domain.GroupRepository;
import org.noostak.group.dto.response.ongoing.*;
import org.noostak.infra.S3Service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupOngoingAppointmentServiceImpl implements GroupOngoingAppointmentService {

    private final AppointmentRecommendationFacade recommendationFacade;
    private final GroupRepository groupRepository;
    private final AppointmentRepository appointmentRepository;
    private final S3Service s3Service;

    @Override
    public GroupOngoingAppointmentsResponse getGroupOngoingAppointments(Long memberId, Long groupId) {
        Group group = findGroupById(groupId);
        List<Appointment> appointments = findAllAppointmentsByGroupId(groupId);

        List<OngoingAppointmentResponse> ongoingAppointments = appointments.stream()
                .map(appointment -> findBestOptionForAppointment(memberId, appointment))
                .flatMap(Optional::stream)
                .collect(Collectors.toList());

        return buildGroupOngoingAppointmentsResponse(group, ongoingAppointments);
    }

    private List<Appointment> findAllAppointmentsByGroupId(Long groupId) {
        return appointmentRepository.findAllByGroupId(groupId);
    }

    private Optional<OngoingAppointmentResponse> findBestOptionForAppointment(Long memberId, Appointment appointment) {
        return recommendationFacade.getRecommendedOptions(memberId, appointment.getId(), appointment).stream()
                .flatMap(priorityGroup -> priorityGroup.options().stream())
                .sorted((o1, o2) -> Long.compare(o2.availableMemberCount(), o1.availableMemberCount()))
                .findFirst()
                .map(option -> buildOngoingAppointmentResponse(appointment, option));
    }

    private GroupOngoingAppointmentsResponse buildGroupOngoingAppointmentsResponse(Group group, List<OngoingAppointmentResponse> ongoingAppointments) {
        return GroupOngoingAppointmentsResponse.of(
                GroupOngoingInfoResponse.of(
                        group.getName().value(),
                        s3Service.getImageUrl(group.getKey().value()),
                        group.getCount().value(),
                        group.getCode().value()
                ),
                ongoingAppointments
        );
    }

    private Group findGroupById(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new AppointmentException(AppointmentErrorCode.GROUP_NOT_FOUND));
    }

    private OngoingAppointmentResponse buildOngoingAppointmentResponse(Appointment appointment, AppointmentOptionResponse recommendedOption) {
        AppointmentOptionTimeResponse optionTime = recommendedOption.appointmentOptionTime();

        return OngoingAppointmentResponse.of(
                appointment.getId(),
                appointment.getName().value(),
                recommendedOption.availableMemberCount(),
                List.of(AppointmentOngoingHostSelectionTimeResponse.of(
                        optionTime.date(),
                        optionTime.startTime(),
                        optionTime.endTime()
                ))
        );
    }
}
