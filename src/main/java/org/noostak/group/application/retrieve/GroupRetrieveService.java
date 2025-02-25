package org.noostak.group.application.retrieve;

import org.noostak.group.dto.response.retrieve.GroupsRetrieveResponse;

public interface GroupRetrieveService {
    GroupsRetrieveResponse findGroups(Long memberId);
}
