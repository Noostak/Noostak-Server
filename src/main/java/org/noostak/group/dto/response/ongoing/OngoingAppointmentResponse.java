package org.noostak.group.dto.response.ongoing;

import java.util.List;

public record OngoingAppointmentResponse(
        Long appointmentId,
        String appointmentName,
        Long availableGroupMemberCount,
        AppointmentOngoingHostSelectionTimeResponse appointmentTime // TODO: appointmentTime 이와 관련된 클래스 명 모두 수정하기
) {
    public static OngoingAppointmentResponse of(
            Long appointmentId,
            String appointmentName,
            Long availableGroupMemberCount,
            AppointmentOngoingHostSelectionTimeResponse hostSelectionTimes
    ) {
        return new OngoingAppointmentResponse(appointmentId, appointmentName, availableGroupMemberCount, hostSelectionTimes);
    }
}
