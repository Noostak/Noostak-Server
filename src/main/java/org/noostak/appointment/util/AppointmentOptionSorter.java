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
        // TreeMap을 사용해 가용성 카운트를 내림차순으로 정렬
        TreeMap<Integer, Set<AppointmentOption>> grouped = new TreeMap<>(Comparator.reverseOrder());

        // 그룹화 작업 수행
        Map<Integer, Set<AppointmentOption>> tempGrouped = availabilityCounts.entrySet().stream()
                .collect(Collectors.groupingBy(
                        Map.Entry::getValue,
                        Collectors.mapping(
                                Map.Entry::getKey,
                                Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(AppointmentOption::getStartTime)))
                        )
                ));

        // 정렬된 TreeMap에 추가
        grouped.putAll(tempGrouped);

        return grouped.values().stream()
                .flatMap(Collection::stream)
                .limit(MAX_RESULTS)
                .collect(Collectors.toList());
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
