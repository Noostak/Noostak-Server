package org.noostak.appointmentoption.converter;

import org.noostak.appointment.domain.Appointment;
import org.noostak.appointmentmember.domain.AppointmentMember;
import org.noostak.appointmentoption.domain.AppointmentOption;
import org.noostak.appointmentoption.dto.response.confirmed.*;

import java.util.List;

public class AppointmentOptionConfirmedOptionConverter {

    public static AppointmentConfirmedOptionResponse toResponse(
            AppointmentOption appointmentOption, Appointment appointment, AppointmentMember targetMember,
            List<String> availableMemberNames, List<String> unavailableMemberNames, int memberIndex) {

        return AppointmentConfirmedOptionResponse.of(
                toTimeResponse(appointmentOption),
                appointment.getCategory().getMessage(),
                appointment.getName().value(),
                toMyInfoResponse(targetMember, memberIndex),
                toAvailableFriendsResponse(availableMemberNames),
                toUnavailableFriendsResponse(unavailableMemberNames)
        );
    }

    private static AppointmentConfirmedOptionTimeResponse toTimeResponse(AppointmentOption appointmentOption) {
        return AppointmentConfirmedOptionTimeResponse.of(
                appointmentOption.getDate(),
                appointmentOption.getStartTime(),
                appointmentOption.getEndTime()
        );
    }

    private static AppointmentConfirmedOptionMyInfoResponse toMyInfoResponse(AppointmentMember targetMember, int memberIndex) {
        return AppointmentConfirmedOptionMyInfoResponse.of(
                targetMember.getAppointmentAvailability().name(),
                memberIndex,
                targetMember.getMember().getName().value()
        );
    }

    private static AppointmentConfirmedOptionAvailableFriendsResponse toAvailableFriendsResponse(List<String> availableMemberNames) {
        return AppointmentConfirmedOptionAvailableFriendsResponse.of(
                availableMemberNames.size(),
                availableMemberNames
        );
    }

    private static AppointmentConfirmedOptionUnavailableFriendsResponse toUnavailableFriendsResponse(List<String> unavailableMemberNames) {
        return AppointmentConfirmedOptionUnavailableFriendsResponse.of(
                unavailableMemberNames.size(),
                unavailableMemberNames
        );
    }
}
