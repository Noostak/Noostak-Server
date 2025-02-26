package org.noostak.appointment.util;

import org.noostak.appointment.dto.response.AppointmentOptionAvailabilityResponse;
import org.noostak.appointmentmember.domain.AppointmentMemberAvailableTime;

import java.util.List;
import java.util.Map;

public class AppointmentAvailabilityMatcher {

    public static List<AppointmentOptionAvailabilityResponse> matchAvailability(
            List<TimeSlot> timeSlots,
            Map<Long, List<AppointmentMemberAvailableTime>> memberAvailability
    ) {
        return timeSlots.stream()
                .map(slot -> {
                    List<Long> availableMembers = getAvailableMembers(slot, memberAvailability);

                    return AppointmentOptionAvailabilityResponse.of(slot.date(), slot.start(), slot.end(), availableMembers);
                })
                .toList();
    }

    public static List<Long> getAvailableMembers(TimeSlot slot, Map<Long, List<AppointmentMemberAvailableTime>> memberAvailability) {
        return memberAvailability.entrySet().stream()
                .filter(entry -> satisfiesDuration(entry.getValue(), slot))
                .map(Map.Entry::getKey)
                .toList();
    }

    public static boolean satisfiesDuration(List<AppointmentMemberAvailableTime> memberTimes, TimeSlot slot) {
        return memberTimes.stream()
                .anyMatch(time -> !time.getStartTime().isAfter(slot.start()) && !time.getEndTime().isBefore(slot.end()));
    }
}
