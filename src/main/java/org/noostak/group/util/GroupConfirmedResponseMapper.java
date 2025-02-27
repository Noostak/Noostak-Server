package org.noostak.group.util;

import org.noostak.appointment.common.exception.AppointmentErrorCode;
import org.noostak.appointment.common.exception.AppointmentException;
import org.noostak.appointment.domain.Appointment;
import org.noostak.appointmentoption.domain.AppointmentOption;
import org.noostak.group.dto.response.confirmed.AppointmentTimeResponse;
import org.noostak.group.dto.response.confirmed.ConfirmedAppointmentsResponse;
import org.noostak.group.dto.response.confirmed.GroupConfirmedInfoResponse;
import org.noostak.group.domain.Group;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GroupConfirmedResponseMapper {

    public static GroupConfirmedInfoResponse toGroupConfirmedInfo(Group group, String groupProfileImageUrl) {
        return GroupConfirmedInfoResponse.of(
                getGroupName(group),
                groupProfileImageUrl,
                getGroupMemberCount(group)
        );
    }

    public static List<ConfirmedAppointmentsResponse> toConfirmedAppointmentsResponse(
            List<Appointment> confirmedAppointments,
            List<AppointmentOption> confirmedOptions
    ) {
        Map<Long, AppointmentOption> appointmentOptionMap = mapConfirmedOptionsByAppointmentId(confirmedOptions);

        return confirmedAppointments.stream()
                .map(appointment -> toConfirmedAppointmentResponse(appointment, appointmentOptionMap))
                .collect(Collectors.toList());
    }

    private static ConfirmedAppointmentsResponse toConfirmedAppointmentResponse(
            Appointment appointment,
            Map<Long, AppointmentOption> appointmentOptionMap
    ) {
        AppointmentOption confirmedOption = findConfirmedOption(appointment.getId(), appointmentOptionMap);

        return ConfirmedAppointmentsResponse.of(
                appointment.getId(),
                getAppointmentName(appointment),
                getAppointmentCategory(appointment),
                toAppointmentTimeResponse(confirmedOption)
        );
    }

    private static AppointmentOption findConfirmedOption(Long appointmentId, Map<Long, AppointmentOption> appointmentOptionMap) {
        AppointmentOption confirmedOption = appointmentOptionMap.get(appointmentId);

        if (confirmedOption == null) {
            throw new AppointmentException(AppointmentErrorCode.CONFIRMED_OPTION_NOT_FOUND);
        }

        return confirmedOption;
    }

    private static Map<Long, AppointmentOption> mapConfirmedOptionsByAppointmentId(List<AppointmentOption> confirmedOptions) {
        return confirmedOptions.stream()
                .collect(Collectors.toMap(
                        option -> option.getAppointment().getId(),
                        Function.identity(),
                        (existing, replacement) -> existing
                ));
    }

    private static AppointmentTimeResponse toAppointmentTimeResponse(AppointmentOption confirmedOption) {
        return AppointmentTimeResponse.of(
                confirmedOption.getDate(),
                confirmedOption.getStartTime(),
                confirmedOption.getEndTime()
        );
    }

    private static String getGroupName(Group group) {
        return group.getName().value();
    }

    private static Long getGroupMemberCount(Group group) {
        return group.getCount().value();
    }

    private static String getAppointmentName(Appointment appointment) {
        return appointment.getName().value();
    }

    private static String getAppointmentCategory(Appointment appointment) {
        return appointment.getCategory().getMessage();
    }
}
