package org.noostak.group.dto.response.retrieve;

import java.util.List;

public record GroupsRetrieveResponse(
        List<GroupRetrieveResponse> groups
) {
    public static GroupsRetrieveResponse of(final List<GroupRetrieveResponse> groupResponses) {
        return new GroupsRetrieveResponse(groupResponses);
    }
}
