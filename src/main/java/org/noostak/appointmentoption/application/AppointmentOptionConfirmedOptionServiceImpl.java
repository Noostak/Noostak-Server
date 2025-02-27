package org.noostak.appointmentoption.application;

import lombok.RequiredArgsConstructor;
import org.noostak.appointment.domain.Appointment;
import org.noostak.appointmentmember.domain.AppointmentMember;
import org.noostak.appointmentmember.domain.AppointmentMemberRepository;
import org.noostak.appointmentmember.domain.vo.AppointmentAvailability;
import org.noostak.appointmentoption.common.exception.AppointmentOptionErrorCode;
import org.noostak.appointmentoption.common.exception.AppointmentOptionException;
import org.noostak.appointmentoption.converter.AppointmentOptionConfirmedOptionConverter;
import org.noostak.appointmentoption.domain.AppointmentOption;
import org.noostak.appointmentoption.domain.AppointmentOptionRepository;
import org.noostak.appointmentoption.domain.vo.AppointmentOptionStatus;
import org.noostak.appointmentoption.dto.response.confirmed.AppointmentConfirmedOptionResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AppointmentOptionConfirmedOptionServiceImpl implements AppointmentOptionConfirmedOptionService {

    private final AppointmentOptionRepository appointmentOptionRepository;
    private final AppointmentMemberRepository appointmentMemberRepository;

    @Override
    public AppointmentConfirmedOptionResponse getConfirmedAppointmentOption(Long memberId, Long appointmentOptionId) {
        AppointmentOption appointmentOption = findConfirmedAppointmentOption(appointmentOptionId);
        Appointment appointment = appointmentOption.getAppointment();
        List<AppointmentMember> appointmentMembers = findAppointmentMembers(appointment.getId());
        AppointmentMember targetMember = findTargetMember(appointmentMembers, memberId);

        Map<AppointmentAvailability, List<String>> groupedMembers = groupMembersByAvailability(appointmentMembers);
        List<String> availableMemberNames = groupedMembers.getOrDefault(AppointmentAvailability.AVAILABLE, List.of());
        List<String> unavailableMemberNames = groupedMembers.getOrDefault(AppointmentAvailability.UNAVAILABLE, List.of());

        int memberIndex = findMemberIndexInLists(targetMember.getMember().getName().value(), availableMemberNames, unavailableMemberNames);

        return AppointmentOptionConfirmedOptionConverter.toResponse(
                appointmentOption, appointment, targetMember, availableMemberNames, unavailableMemberNames, memberIndex
        );
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
