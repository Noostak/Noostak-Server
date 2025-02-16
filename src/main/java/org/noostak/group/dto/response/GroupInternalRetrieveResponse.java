package org.noostak.group.dto.response;

import org.noostak.group.domain.Group;

public record GroupInternalRetrieveResponse(
        Group group,
        String groupProfileImageUrl
) {
    public static GroupInternalRetrieveResponse of(final Group group, final String groupProfileImageUrl) {
        return new GroupInternalRetrieveResponse(
                group,
                groupProfileImageUrl
        );
    }
}
