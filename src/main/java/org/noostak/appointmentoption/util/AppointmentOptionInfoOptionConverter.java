package org.noostak.appointmentoption.util;

import org.noostak.appointment.domain.Appointment;
import org.noostak.appointmentmember.domain.AppointmentMember;
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
                toMyInfoResponse(targetMember, memberIndex),
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

    private static AppointmentOptionInfoMyInfoResponse toMyInfoResponse(AppointmentMember targetMember, int memberIndex) {
        return AppointmentOptionInfoMyInfoResponse.of(
                targetMember.getAppointmentAvailability().getMessage(),
                memberIndex,
                targetMember.getMember().getName().value()
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