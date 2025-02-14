package org.noostak.group.dto.response;

import lombok.Builder;
import org.noostak.group.domain.Group;

@Builder
public record GroupCreateResponse(
        Long groupId,
        String groupName,
        String groupProfileImageUrl,
        String groupInvitationCode
) {
    public static GroupCreateResponse of(final Group group, final String groupProfileImageUrl) {
        {
            return GroupCreateResponse.builder()
                    .groupId(group.getGroupId())
                    .groupName(group.getName().value())
                    .groupProfileImageUrl(groupProfileImageUrl)
                    .groupInvitationCode(group.getCode().value())
                    .build();
        }
    }
}
