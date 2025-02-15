package org.noostak.group.application;

import lombok.RequiredArgsConstructor;
import org.noostak.group.common.exception.GroupErrorCode;
import org.noostak.group.common.exception.GroupException;
import org.noostak.group.domain.Groups;
import org.noostak.group.dto.response.GroupInternalRetrieveResponse;
import org.noostak.group.dto.response.GroupRetrieveResponse;
import org.noostak.group.dto.response.GroupsRetrieveResponse;
import org.noostak.infra.S3Service;
import org.noostak.membergroup.domain.MemberGroup;
import org.noostak.membergroup.domain.MemberGroupRepository;
import org.noostak.membergroup.domain.MemberGroups;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupRetrieveServiceImpl implements GroupRetrieveService {

    private final MemberGroupRepository memberGroupRepository;
    private final S3Service s3Service;

    @Override
    public GroupsRetrieveResponse findGroups(Long memberId) {
        MemberGroups memberGroups = getMemberGroups(memberId);
        Groups groups = convertToGroups(memberGroups);
        return convertToResponse(groups);
    }

    private MemberGroups getMemberGroups(Long memberId) {
        List<MemberGroup> foundMemberGroups = memberGroupRepository.findByMemberId(memberId);
        MemberGroups memberGroups = MemberGroups.of(foundMemberGroups);

        if (memberGroups.isEmpty()) {
            throw new GroupException(GroupErrorCode.GROUP_NOT_FOUND);
        }

        return memberGroups;
    }

    private Groups convertToGroups(MemberGroups memberGroups) {
        return Groups.of(memberGroups.toGroups());
    }

    private GroupsRetrieveResponse convertToResponse(Groups groups) {
        List<GroupInternalRetrieveResponse> internalResponses = groups.getGroups().stream()
                .map(group -> GroupInternalRetrieveResponse.of(group, s3Service.getImageUrl(group.getKey().value())))
                .toList();

        List<GroupRetrieveResponse> groupResponses = internalResponses.stream()
                .map(internal -> GroupRetrieveResponse.of(internal.group(), internal.groupProfileImageUrl()))
                .toList();

        return GroupsRetrieveResponse.of(groupResponses);
    }
}
