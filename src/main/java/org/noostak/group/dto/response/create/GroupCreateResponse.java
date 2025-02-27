package org.noostak.group.dto.response.create;

import org.noostak.group.domain.Group;

public record GroupCreateResponse(
        Long groupId,
        String groupName,
        String groupProfileImageUrl,
        String groupInvitationCode
) {
    public static GroupCreateResponse of(final Group group, final String groupProfileImageUrl) {
        return new GroupCreateResponse(
                group.getId(),
                group.getName().value(),
                groupProfileImageUrl,
                group.getCode().value()
        );
    }
}