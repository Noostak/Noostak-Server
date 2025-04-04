package org.noostak.appointmentoption.util;

import org.noostak.appointment.domain.Appointment;
import org.noostak.appointmentmember.domain.AppointmentMember;
import org.noostak.appointmentmember.domain.vo.AppointmentAvailability;
import org.noostak.appointmentoption.domain.AppointmentOption;
import org.noostak.appointmentoption.dto.response.info.*;

import java.util.List;

public class AppointmentOptionInfoOptionConverter {

    public static AppointmentOptionInfoResponse toResponse(
            AppointmentOption appointmentOption, Appointment appointment, AppointmentMember targetMember,
            List<String> availableMemberNames, List<String> unavailableMemberNames, int memberIndex) {

        return AppointmentOptionInfoResponse.of(
                toTimeResponse(appointmentOption),
                appointment.getCategory().getMessage(),
                appointment.getName().value(),
                toMyInfoResponse(availableMemberNames, targetMember, memberIndex),
                toAvailableFriendsResponse(availableMemberNames),
                toUnavailableFriendsResponse(unavailableMemberNames)
        );
    }

    private static AppointmentOptionInfoTimeResponse toTimeResponse(AppointmentOption appointmentOption) {
        return AppointmentOptionInfoTimeResponse.of(
                appointmentOption.getDate(),
                appointmentOption.getStartTime(),
                appointmentOption.getEndTime()
        );
    }

    private static AppointmentOptionInfoMyInfoResponse toMyInfoResponse(
            List<String> availableMemberNames,
            AppointmentMember targetMember,
            int memberIndex) {
        String targetMemberName = targetMember.getMember().getName().value();
        AppointmentAvailability appointmentAvailability = targetMember.getAppointmentAvailability();

        // 만약, 가능한 멤버에 속해있다면 AVAILABLE로 반환하기
        if(availableMemberNames.contains(targetMemberName)){
            appointmentAvailability = AppointmentAvailability.AVAILABLE;
        }

        return AppointmentOptionInfoMyInfoResponse.of(
                appointmentAvailability.getMessage(),
                memberIndex,
                targetMemberName
        );
    }

    private static AppointmentOptionInfoAvailableFriendsResponse toAvailableFriendsResponse(List<String> availableMemberNames) {
        return AppointmentOptionInfoAvailableFriendsResponse.of(
                availableMemberNames.size(),
                availableMemberNames
        );
    }

    private static AppointmentOptionInfoUnavailableFriendsResponse toUnavailableFriendsResponse(List<String> unavailableMemberNames) {
        return AppointmentOptionInfoUnavailableFriendsResponse.of(
                unavailableMemberNames.size(),
                unavailableMemberNames
        );
    }
}