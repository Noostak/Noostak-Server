package org.noostak.group.dto.response.info;

public record GroupMemberInfoResponse(
        String memberName,
        String memberProfileImageUrl
) {
    public static GroupMemberInfoResponse of(final String memberName, final String memberProfileImageUrl) {
        return new GroupMemberInfoResponse(memberName, memberProfileImageUrl);
    }
}
