package org.noostak.appointment.util;

import org.noostak.appointment.common.exception.AppointmentErrorCode;
import org.noostak.appointment.common.exception.AppointmentException;
import org.noostak.appointment.dto.response.recommendation.*;
import org.noostak.appointmentmember.domain.vo.AppointmentAvailability;
import org.noostak.appointmentoption.domain.AppointmentOption;
import org.noostak.appointmentmember.domain.AppointmentMemberAvailableTime;
import org.noostak.group.domain.GroupRepository;
import org.noostak.likes.domain.LikeRepository;

import java.util.List;
import java.util.Map;

public class AppointmentOptionResponseMapper {

    public static AppointmentOptionResponse toResponse(
            AppointmentOption option,
            Long memberId,
            Map<Long, List<AppointmentMemberAvailableTime>> memberAvailability,
            Map<Long, String> memberNames,
            LikeRepository likeRepository,
            GroupRepository groupRepository
    ) {
        List<Long> availableMembers = findAvailableMembers(option, memberAvailability);
        List<Long> unavailableMembers = findUnavailableMembers(memberAvailability, availableMembers);
        String myName = findMemberName(memberId, memberNames);
        boolean isAvailable = availableMembers.contains(memberId);
        Long position = findMemberPosition(isAvailable, memberId, availableMembers, unavailableMembers);
        String availability = determineAvailability(isAvailable);

        return createAppointmentOptionResponse(
                option, memberId, availableMembers, unavailableMembers,
                availability, position, myName,
                likeRepository, groupRepository,
                memberNames
        );
    }


    private static List<Long> findAvailableMembers(
            AppointmentOption option,
            Map<Long, List<AppointmentMemberAvailableTime>> memberAvailability
    ) {
        return memberAvailability.entrySet().stream()
                .filter(entry -> AppointmentAvailabilityMatcher.satisfiesDuration(
                        entry.getValue(),
                        TimeSlot.of(option.getDate(), option.getStartTime(), option.getEndTime())
                ))
                .map(Map.Entry::getKey)
                .toList();
    }

    private static List<Long> findUnavailableMembers(
            Map<Long, List<AppointmentMemberAvailableTime>> memberAvailability,
            List<Long> availableMembers
    ) {
        return memberAvailability.keySet().stream()
                .filter(id -> !availableMembers.contains(id))
                .toList();
    }

    private static Long findMemberPosition(
            boolean isAvailable,
            Long memberId,
            List<Long> availableMembers,
            List<Long> unavailableMembers
    ) {
        if (isAvailable) {
            return (long) availableMembers.indexOf(memberId);
        }
        return (long) unavailableMembers.indexOf(memberId);
    }

    private static String determineAvailability(boolean isAvailable) {
        if (isAvailable) {
            return AppointmentAvailability.AVAILABLE.getMessage();
        }
        return AppointmentAvailability.UNAVAILABLE.getMessage();
    }

    private static String findMemberName(Long memberId, Map<Long, String> memberNames) {
        if (!memberNames.containsKey(memberId)) {
            throw new AppointmentException(AppointmentErrorCode.MEMBER_NOT_FOUND);
        }
        return memberNames.get(memberId);
    }

    private static Long countLikes(AppointmentOption option, LikeRepository likeRepository) {
        return likeRepository.getLikeCountByOptionId(option.getId());
    }

    private static boolean checkIfLiked(AppointmentOption option, Long memberId, LikeRepository likeRepository) {
        return likeRepository.getExistsByAppointmentOptionIdAndAppointmentMemberId(option.getId(), memberId);
    }

    private static Long findGroupMemberCount(AppointmentOption option, GroupRepository groupRepository) {
        return groupRepository.findById(option.getAppointment().getGroup().getId())
                .map(group -> group.getCount().value())
                .orElse(0L);
    }

    private static AppointmentOptionResponse createAppointmentOptionResponse(
            AppointmentOption option,
            Long memberId,
            List<Long> availableMembers,
            List<Long> unavailableMembers,
            String availability,
            Long position,
            String myName,
            LikeRepository likeRepository,
            GroupRepository groupRepository,
            Map<Long, String> memberNames
    ) {
        Long likeCount = countLikes(option, likeRepository);
        boolean liked = checkIfLiked(option, memberId, likeRepository);
        Long groupMemberCount = findGroupMemberCount(option, groupRepository);

        List<String> availableNames = mapMemberIdsToNames(availableMembers, memberNames);
        List<String> unavailableNames = mapMemberIdsToNames(unavailableMembers, memberNames);

        return AppointmentOptionResponse.of(
                option.getId(), likeCount, liked, groupMemberCount,
                (long) availableMembers.size(),
                AppointmentOptionTimeResponse.of(option.getStartTime(), option.getStartTime(), option.getEndTime()),
                AppointmentMyInfoResponse.of(availability, position, myName),
                AvailableFriendsResponse.of((long) availableNames.size(), availableNames),
                UnavailableFriendsResponse.of((long) unavailableNames.size(), unavailableNames)
        );
    }


    private static List<String> mapMemberIdsToNames(List<Long> memberIds, Map<Long, String> memberNames) {
        return memberIds.stream().map(memberNames::get).toList();
    }
}
