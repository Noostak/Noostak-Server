package org.noostak.group.dto.response.info;

import java.util.List;

public record GroupSummaryResponse(
        GroupMemberInfoResponse groupHostInfo,
        String groupName,
        String groupProfileImageUrl,
        Long groupMemberCount,
        String groupInvitationCode,
        List<GroupMemberInfoResponse> groupMemberInfo
) {
    public static GroupSummaryResponse of(final GroupMemberInfoResponse groupHostInfo, final String groupName, final String groupProfileImageUrl, final Long groupMemberCount, final String groupInvitationCode, final List<GroupMemberInfoResponse> groupMemberInfo) {
        return new GroupSummaryResponse(groupHostInfo, groupName, groupProfileImageUrl, groupMemberCount, groupInvitationCode, groupMemberInfo);}
}
