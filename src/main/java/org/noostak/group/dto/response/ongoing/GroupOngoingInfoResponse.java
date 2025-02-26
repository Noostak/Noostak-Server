package org.noostak.group.dto.response.ongoing;

public record GroupOngoingInfoResponse(
        String groupName,
        String groupProfileImageUrl,
        Long groupMemberCount,
        String groupInviteCode
) {
    public static GroupOngoingInfoResponse of(
            String groupName,
            String groupProfileImageUrl,
            Long groupMemberCount,
            String groupInviteCode
    ) {
        return new GroupOngoingInfoResponse(groupName, groupProfileImageUrl, groupMemberCount, groupInviteCode);
    }
}
