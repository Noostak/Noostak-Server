package org.noostak.group.dto.response.confirmed;

public record GroupConfirmedInfoResponse(
        String groupName,
        String groupProfileImageUrl,
        Long groupMemberCount
) {
    public static GroupConfirmedInfoResponse of(String groupName, String groupProfileImageUrl, Long groupMemberCount) {
        return new GroupConfirmedInfoResponse(groupName, groupProfileImageUrl, groupMemberCount);
    }
}
