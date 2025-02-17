package org.noostak.group.dto.response.create;


import org.noostak.group.domain.Group;

public record GroupCreateInternalResponse(
        Group group,
        String groupProfileImageUrl
) {
    public static GroupCreateInternalResponse of(final Group group, final String groupProfileImageUrl) {
        {
            return new GroupCreateInternalResponse(group, groupProfileImageUrl);
        }
    }
}
