package org.noostak.group.dto.response;

import org.noostak.group.domain.Group;

public record GroupRetrieveResponse(
        Long groupId,
        String groupName,
        Long groupMemberCount,
        String groupProfileImageUrl
) {
    public static GroupRetrieveResponse of(final Group group, final String groupProfileImageUrl) {
        return new GroupRetrieveResponse(
                group.getGroupId(),
                group.getName().value(),
                group.getCount().value(),
                groupProfileImageUrl
        );
    }
}