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
    public AppointmentMembersAvailableTimesResponse getAvailableTimes(Long memberId, Long appointmentId) {
        AppointmentMember appointmentMember = findAppointmentMember(memberId, appointmentId);

        boolean appointmentTimeSet = appointmentMember.isAppointmentTimeSet();

        AppointmentHostSelectionTimesResponse hostSelectionTimesResponse = findHostSelectionTimes(appointmentId);

        List<AppointmentMemberInfoResponse> appointmentMembersInfo = findAppointmentMembersInfo(appointmentId);

        return createAppointmentResponse(appointmentTimeSet, hostSelectionTimesResponse, appointmentMembersInfo);
    }

    private AppointmentMember findAppointmentMember(Long memberId, Long appointmentId) {
        return appointmentMemberRepository.findByMemberIdAndAppointmentId(memberId, appointmentId)
                .orElseThrow(() -> new AppointmentMemberException(AppointmentMemberErrorCode.APPOINTMENT_MEMBER_NOT_FOUND));
    }

    private AppointmentHostSelectionTimesResponse findHostSelectionTimes(Long appointmentId) {
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

    private List<AppointmentMemberInfoResponse> findAppointmentMembersInfo(Long appointmentId) {
        return appointmentMemberRepository.findAllWithAvailableTimes(appointmentId).stream()
                .map(member -> {
                    List<AppointmentMemberAvailableTimeResponse> availableTimeDtos = findAvailableTimesForMember(member);
                    return AppointmentMemberInfoResponse.of(
                            member.getId(),
                            member.getMember().getName().value(),
                            AppointmentMemberAvailableTimesResponse.of(availableTimeDtos)
                    );
                })
                .collect(Collectors.toList());
    }

    private List<AppointmentMemberAvailableTimeResponse> findAvailableTimesForMember(AppointmentMember member) {
        return appointmentMemberAvailableTimesRepository.findByAppointmentMember(member).stream()
                .map(time -> new AppointmentMemberAvailableTimeResponse(
                        time.getDate(),
                        time.getStartTime(),
                        time.getEndTime()
                ))
                .collect(Collectors.toList());
    }

    private AppointmentMembersAvailableTimesResponse createAppointmentResponse(
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
