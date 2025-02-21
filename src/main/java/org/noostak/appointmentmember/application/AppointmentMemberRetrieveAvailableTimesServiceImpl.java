package org.noostak.appointmentmember.application;

import lombok.RequiredArgsConstructor;
import org.noostak.appointment.domain.AppointmentHostSelectionTimeRepository;
import org.noostak.appointmentmember.common.exception.AppointmentMemberErrorCode;
import org.noostak.appointmentmember.common.exception.AppointmentMemberException;
import org.noostak.appointmentmember.domain.AppointmentMember;
import org.noostak.appointmentmember.domain.AppointmentMemberAvailableTimesRepository;
import org.noostak.appointmentmember.domain.AppointmentMemberRepository;
import org.noostak.appointmentmember.dto.response.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AppointmentMemberRetrieveAvailableTimesServiceImpl implements AppointmentMemberRetrieveAvailableTimesService {

    private final AppointmentHostSelectionTimeRepository appointmentHostSelectionTimeRepository;
    private final AppointmentMemberAvailableTimesRepository appointmentMemberAvailableTimesRepository;
    private final AppointmentMemberRepository appointmentMemberRepository;

    @Override
    public AppointmentMembersAvailableTimesResponse retrieveAvailableTimes(Long memberId, Long appointmentId) {
        AppointmentMember appointmentMember = getAppointmentMemberOrThrow(memberId, appointmentId);

        boolean appointmentTimeSet = appointmentMember.isAppointmentTimeSet();

        AppointmentHostSelectionTimesResponse hostSelectionTimesResponse = retrieveHostSelectionTimes(appointmentId);

        List<AppointmentMemberInfoResponse> appointmentMembersInfo = retrieveAppointmentMembersInfo(appointmentId);

        return assembleAppointmentResponse(appointmentTimeSet, hostSelectionTimesResponse, appointmentMembersInfo);
    }

    private AppointmentMember getAppointmentMemberOrThrow(Long memberId, Long appointmentId) {
        return appointmentMemberRepository.findByMemberIdAndAppointmentId(memberId, appointmentId)
                .orElseThrow(() -> new AppointmentMemberException(AppointmentMemberErrorCode.APPOINTMENT_MEMBER_NOT_FOUND));
    }

    private AppointmentHostSelectionTimesResponse retrieveHostSelectionTimes(Long appointmentId) {
        List<AppointmentHostSelectionTimeResponse> hostSelectionTimeDtos =
                appointmentHostSelectionTimeRepository.findByAppointmentId(appointmentId).stream()
                        .map(time -> AppointmentHostSelectionTimeResponse.of(
                                time.getDate(),
                                time.getStartTime(),
                                time.getEndTime()
                        ))
                        .collect(Collectors.toList());

        return AppointmentHostSelectionTimesResponse.of(hostSelectionTimeDtos);
    }

    private List<AppointmentMemberInfoResponse> retrieveAppointmentMembersInfo(Long appointmentId) {
        return appointmentMemberRepository.findAllWithAvailableTimes(appointmentId).stream()
                .map(member -> {
                    List<AppointmentMemberAvailableTimeResponse> availableTimeDtos = retrieveAvailableTimesForMember(member);
                    return AppointmentMemberInfoResponse.of(
                            member.getId(),
                            member.getMember().getName().value(),
                            AppointmentMemberAvailableTimesResponse.of(availableTimeDtos)
                    );
                })
                .collect(Collectors.toList());
    }

    private List<AppointmentMemberAvailableTimeResponse> retrieveAvailableTimesForMember(AppointmentMember member) {
        return appointmentMemberAvailableTimesRepository.findByAppointmentMember(member).stream()
                .map(time -> new AppointmentMemberAvailableTimeResponse(
                        time.getDate(),
                        time.getStartTime(),
                        time.getEndTime()
                ))
                .collect(Collectors.toList());
    }

    private AppointmentMembersAvailableTimesResponse assembleAppointmentResponse(
            boolean appointmentTimeSet,
            AppointmentHostSelectionTimesResponse hostSelectionTimesResponse,
            List<AppointmentMemberInfoResponse> appointmentMembersInfo) {

        AppointmentScheduleResponse appointmentScheduleResponse = AppointmentScheduleResponse.of(
                hostSelectionTimesResponse,
                appointmentMembersInfo
        );

        return AppointmentMembersAvailableTimesResponse.of(appointmentTimeSet, appointmentScheduleResponse);
    }
}
