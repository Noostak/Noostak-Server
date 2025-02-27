package org.noostak.group.application.info;

import lombok.RequiredArgsConstructor;
import org.noostak.group.common.exception.GroupErrorCode;
import org.noostak.group.common.exception.GroupException;
import org.noostak.group.domain.Group;
import org.noostak.group.domain.GroupRepository;
import org.noostak.group.dto.response.info.GroupInfoResponse;
import org.noostak.group.dto.response.info.GroupMemberInfoResponse;
import org.noostak.group.dto.response.info.GroupSummaryResponse;
import org.noostak.infra.S3Service;
import org.noostak.member.domain.Member;
import org.noostak.membergroup.domain.MemberGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupInfoServiceImpl implements GroupInfoService {

    private final GroupRepository groupRepository;
    private final MemberGroupRepository memberGroupRepository;
    private final S3Service s3Service;

    @Override
    public GroupInfoResponse getGroupInfo(final Long memberId, final Long groupId) {
        Group group = findGroupById(groupId);

        Member member = findMemberInGroup(memberId, groupId);
        GroupMemberInfoResponse myInfoResponse = convertToGroupMemberInfo(member);

        GroupMemberInfoResponse groupHostInfo = findGroupHost(groupId);

        List<GroupMemberInfoResponse> groupMembersInfo = findGroupMembers(groupId);

        GroupSummaryResponse groupSummaryResponse = convertToGroupSummaryResponse(group, groupHostInfo, groupMembersInfo);

        return GroupInfoResponse.of(myInfoResponse, groupSummaryResponse);
    }

    private Member findMemberInGroup(Long memberId, Long groupId) {
        return memberGroupRepository.findMembersByGroupId(groupId).stream()
                .filter(m -> m.getId().equals(memberId))
                .findFirst()
                .orElseThrow(() -> new GroupException(GroupErrorCode.MEMBER_NOT_FOUND));
    }

    private Group findGroupById(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupException(GroupErrorCode.GROUP_NOT_FOUND));
    }

    private GroupMemberInfoResponse findGroupHost(Long groupId) {
        Member host = memberGroupRepository.findGroupHostByGroupId(groupId);
        if (host == null) throw new GroupException(GroupErrorCode.GROUP_MEMBER_NOT_FOUND);

        return convertToGroupMemberInfo(host);
    }

    private List<GroupMemberInfoResponse> findGroupMembers(Long groupId) {
        return memberGroupRepository.findMembersByGroupId(groupId).stream()
                .map(this::convertToGroupMemberInfo)
                .toList();
    }

    private GroupMemberInfoResponse convertToGroupMemberInfo(Member member) {
        return GroupMemberInfoResponse.of(
                member.getName().value(),
                s3Service.getImageUrl(member.getKey().value())
        );
    }

    private GroupSummaryResponse convertToGroupSummaryResponse(Group group, GroupMemberInfoResponse hostInfo, List<GroupMemberInfoResponse> membersInfo) {
        String groupProfileImageUrl = getGroupProfileImageUrl(group);

        return GroupSummaryResponse.of(
                hostInfo,
                group.getName().value(),
                groupProfileImageUrl,
                group.getCount().value(),
                group.getCode().value(),
                membersInfo
        );
    }

    private String getGroupProfileImageUrl(Group group) {
        return s3Service.getImageUrl(group.getKey().value());
    }
}
