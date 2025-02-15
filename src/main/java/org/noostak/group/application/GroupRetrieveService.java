package org.noostak.group.application;

import org.noostak.group.dto.response.GroupsRetrieveResponse;

public interface GroupRetrieveService {
    GroupsRetrieveResponse findGroups(Long memberId);
}
