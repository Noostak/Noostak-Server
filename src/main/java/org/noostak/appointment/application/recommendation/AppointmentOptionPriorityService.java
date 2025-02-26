package org.noostak.appointment.application.recommendation;

import org.noostak.appointment.dto.response.AppointmentPriorityGroupResponse;
import org.noostak.appointmentmember.domain.AppointmentMemberAvailableTime;
import org.noostak.appointmentoption.domain.AppointmentOption;

import java.util.List;
import java.util.Map;

public interface AppointmentOptionPriorityService {
    List<AppointmentPriorityGroupResponse> determineOptionPriority(
            List<AppointmentOption> sortedOptions, Long memberId,
            Map<Long, List<AppointmentMemberAvailableTime>> memberAvailability,
            Map<Long, String> memberNames
    );
}
