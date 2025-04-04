package org.noostak.appointmentoption.application.info;

import lombok.RequiredArgsConstructor;
import org.noostak.appointment.domain.Appointment;
import org.noostak.appointment.util.AppointmentAvailabilityMatcher;
import org.noostak.appointment.util.TimeSlot;
import org.noostak.appointmentmember.domain.AppointmentMember;
import org.noostak.appointmentmember.domain.AppointmentMemberAvailableTime;
import org.noostak.appointmentmember.domain.AppointmentMemberAvailableTimesRepository;
import org.noostak.appointmentmember.domain.AppointmentMemberRepository;
import org.noostak.appointmentmember.domain.vo.AppointmentAvailability;
import org.noostak.appointmentoption.common.exception.AppointmentOptionErrorCode;
import org.noostak.appointmentoption.common.exception.AppointmentOptionException;
import org.noostak.appointmentoption.domain.AppointmentOption;
import org.noostak.appointmentoption.domain.AppointmentOptionRepository;
import org.noostak.appointmentoption.domain.vo.AppointmentOptionStatus;
import org.noostak.appointmentoption.dto.response.info.AppointmentOptionInfoResponse;
import org.noostak.appointmentoption.util.AppointmentOptionInfoOptionConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AppointmentOptionInfoServiceImpl implements AppointmentOptionInfoService {

    private final AppointmentOptionRepository appointmentOptionRepository;
    private final AppointmentMemberRepository appointmentMemberRepository;
    private final AppointmentMemberAvailableTimesRepository availableTimesRepository;

    @Override
    public AppointmentOptionInfoResponse getAppointmentOptionInfo(Long memberId, Long appointmentOptionId) {
        AppointmentOption appointmentOption = findByAppointmentOptionId(appointmentOptionId);
        Appointment appointment = appointmentOption.getAppointment();
        List<AppointmentMember> appointmentMembers = findAppointmentMembers(appointment.getId());
        AppointmentMember targetMember = findTargetMember(appointmentMembers, memberId);

        List<String> availableMemberNames =
                getAvailableMemberNames(appointmentOption, appointmentMembers);

        List<String> unavailableMemberNames =
                getUnavailableMembers(appointmentMembers, availableMemberNames);

        int memberIndex = findMemberIndexInLists(targetMember.getMember().getName().value(), availableMemberNames, unavailableMemberNames);

        return AppointmentOptionInfoOptionConverter.toResponse(
                appointmentOption, appointment, targetMember, availableMemberNames, unavailableMemberNames, memberIndex
        );
    }

    private static List<String> getUnavailableMembers(List<AppointmentMember> appointmentMembers,
                                                      List<String> availableMemberNames) {
        return appointmentMembers.stream()
                .filter(appointmentMember -> !availableMemberNames.contains(appointmentMember.getMember().getName().value()))
                .map(appointmentMember -> appointmentMember.getMember().getName().value())
                .toList();
    }

    private List<String> getAvailableMemberNames(AppointmentOption appointmentOption,
                                                 List<AppointmentMember> appointmentMembers) {
        return appointmentMembers.stream()
                .filter(appointmentMember -> isAvailableMember(appointmentOption, appointmentMember))
                .map(appointmentMember -> appointmentMember.getMember().getName().value())
                .toList();
    }

    private boolean isAvailableMember(AppointmentOption option, AppointmentMember appointmentMember){
        // 약속 멤버에 대한 가능한 시간 약속들을 조회
        List<AppointmentMemberAvailableTime> memberTimes =
                availableTimesRepository.findByAppointmentMember(appointmentMember);

        // 가능한 시간이 입력되지 않은 상태이거나, 가능한 시간이 비어있다면 해당 옵션에서 불가능한 멤버로 판단
        if(!appointmentMember.isAppointmentTimeSet() || memberTimes.isEmpty()){
            return false;
        }

        // 옵션에 대해 시간을 만족하는지 확인
        return AppointmentAvailabilityMatcher
                .satisfiesDuration(
                        memberTimes,
                        TimeSlot.of(option.getDate(), option.getStartTime(), option.getEndTime()));
    }

    private AppointmentOption findByAppointmentOptionId(Long appointmentOptionId) {
        return appointmentOptionRepository.getByAppointmentOptionId(appointmentOptionId);
    }

    private AppointmentOption findConfirmedAppointmentOption(Long appointmentOptionId) {
        return appointmentOptionRepository.findById(appointmentOptionId)
                .filter(option -> option.getStatus().equals(AppointmentOptionStatus.CONFIRMED))
                .orElseThrow(() -> new AppointmentOptionException(AppointmentOptionErrorCode.APPOINTMENT_OPTION_NOT_FOUND));
    }

    private List<AppointmentMember> findAppointmentMembers(Long appointmentId) {
        return appointmentMemberRepository.findByAppointmentId(appointmentId);
    }

    private AppointmentMember findTargetMember(List<AppointmentMember> appointmentMembers, Long memberId) {
        return appointmentMembers.stream()
                .filter(member -> member.getMember().getId().equals(memberId))
                .findFirst()
                .orElseThrow(() -> new AppointmentOptionException(AppointmentOptionErrorCode.APPOINTMENT_MEMBER_NOT_FOUND));
    }

    private Map<AppointmentAvailability, List<String>> groupMembersByAvailability(List<AppointmentMember> appointmentMembers) {
        return appointmentMembers.stream()
                .collect(Collectors.groupingBy(
                        AppointmentMember::getAppointmentAvailability,
                        Collectors.mapping(member -> member.getMember().getName().value(), Collectors.toList())
                ));
    }

    private int findMemberIndexInLists(String memberName, List<String> availableMembers, List<String> unavailableMembers) {
        if (availableMembers.contains(memberName)) {
            return availableMembers.indexOf(memberName);
        }
        if (unavailableMembers.contains(memberName)) {
            return unavailableMembers.indexOf(memberName);
        }
        return -1;
    }


}
