package org.noostak.appointment.util;

import org.noostak.appointmentoption.domain.AppointmentOption;
import org.noostak.appointmentmember.domain.AppointmentMemberAvailableTime;

import java.util.*;
import java.util.stream.Collectors;

public class AppointmentOptionSorter {

    private static final int MAX_RESULTS = 5;

    public static List<AppointmentOption> sortAndFilterOptions(
            List<AppointmentOption> options,
            Map<Long, List<AppointmentMemberAvailableTime>> memberAvailability
    ) {
        Map<AppointmentOption, Integer> optionAvailabilityCounts = calculateOptionAvailability(options, memberAvailability);

        return groupByAvailability(optionAvailabilityCounts);
    }

    private static Map<AppointmentOption, Integer> calculateOptionAvailability(
            List<AppointmentOption> options,
            Map<Long, List<AppointmentMemberAvailableTime>> memberAvailability
    ) {
        return options.stream()
                .collect(Collectors.toMap(
                        option -> option,
                        option -> calculateAvailableMembers(option, memberAvailability)
                ));
    }

    private static List<AppointmentOption> groupByAvailability(Map<AppointmentOption, Integer> availabilityCounts) {
        return availabilityCounts.entrySet().stream()
                .collect(Collectors.groupingBy(
                        Map.Entry::getValue,
                        TreeMap::new,
                        Collectors.mapping(Map.Entry::getKey, Collectors.toList())
                ))
                .descendingMap()
                .values().stream()
                .flatMap(List::stream)
                .limit(MAX_RESULTS)
                .toList();
    }

    private static int calculateAvailableMembers(AppointmentOption option,
                                                 Map<Long, List<AppointmentMemberAvailableTime>> memberAvailability) {
        TimeSlot timeSlot = getTimeSlot(option);
        return (int) memberAvailability.values().stream()
                .filter(times -> AppointmentAvailabilityMatcher.satisfiesDuration(times, timeSlot))
                .count();
    }

    private static TimeSlot getTimeSlot(AppointmentOption option) {
        return TimeSlot.of(option.getDate(), option.getStartTime(), option.getEndTime());
    }
}
