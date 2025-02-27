package org.noostak.group.dto.response.info;

public record GroupInfoResponse(
        GroupMemberInfoResponse myInfo,
        GroupSummaryResponse groupInfo
) {
    public static GroupInfoResponse of(final GroupMemberInfoResponse myInfo, final GroupSummaryResponse groupInfo) {
        return new GroupInfoResponse(myInfo, groupInfo);
    }
}
