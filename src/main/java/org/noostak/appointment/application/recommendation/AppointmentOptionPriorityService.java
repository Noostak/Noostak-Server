package org.noostak.appointment.application.recommendation;

import lombok.RequiredArgsConstructor;
import org.noostak.appointment.util.TimeSlot;
import org.noostak.appointment.dto.response.AppointmentPriorityGroupResponse;
import org.noostak.appointment.util.AppointmentAvailabilityMatcher;
import org.noostak.appointment.util.AppointmentOptionResponseMapper;
import org.noostak.appointmentmember.domain.AppointmentMemberAvailableTime;
import org.noostak.appointmentoption.domain.AppointmentOption;
import org.noostak.group.domain.GroupRepository;
import org.noostak.likes.domain.LikeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AppointmentOptionPriorityService {

    private final LikeRepository likeRepository;
    private final GroupRepository groupRepository;

    public List<AppointmentPriorityGroupResponse> determineOptionPriority(
            List<AppointmentOption> sortedOptions, Long memberId,
            Map<Long, List<AppointmentMemberAvailableTime>> memberAvailability,
            Map<Long, String> memberNames
    ) {
        Map<AppointmentOption, Long> availabilityCountMap = calculateAvailabilityCount(sortedOptions, memberAvailability);

        Map<Long, List<AppointmentOption>> groupedOptions = categorizeOptionsByPriority(availabilityCountMap);

        return mapToPriorityGroups(groupedOptions, memberId, memberAvailability, memberNames);
    }

    private Map<AppointmentOption, Long> calculateAvailabilityCount(
            List<AppointmentOption> options,
            Map<Long, List<AppointmentMemberAvailableTime>> memberAvailability
    ) {
        return options.stream()
                .collect(Collectors.toMap(
                        option -> option,
                        option -> (long) AppointmentAvailabilityMatcher.getAvailableMembers(
                                        TimeSlot.of(option.getDate(), option.getStartTime(), option.getEndTime()), memberAvailability)
                                .size()
                ));
    }

    private Map<Long, List<AppointmentOption>> categorizeOptionsByPriority(
            Map<AppointmentOption, Long> availabilityMap
    ) {
        return availabilityMap.entrySet().stream()
                .collect(Collectors.groupingBy(
                        Map.Entry::getValue,
                        Collectors.mapping(Map.Entry::getKey, Collectors.toList())
                ));
    }

    private List<AppointmentPriorityGroupResponse> mapToPriorityGroups(
            Map<Long, List<AppointmentOption>> groupedOptions,
            Long memberId,
            Map<Long, List<AppointmentMemberAvailableTime>> memberAvailability,
            Map<Long, String> memberNames
    ) {
        return groupedOptions.entrySet().stream()
                .map(entry -> AppointmentPriorityGroupResponse.of(entry.getKey(),
                        entry.getValue().stream()
                                .map(option -> AppointmentOptionResponseMapper.toResponse(
                                        option, memberId, memberAvailability, memberNames, likeRepository, groupRepository))
                                .toList()))
                .sorted((a, b) -> Long.compare(a.priority(), b.priority()))
                .toList();
    }
}
