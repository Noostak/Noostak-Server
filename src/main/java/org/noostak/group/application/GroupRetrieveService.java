package org.noostak.group.application;

import org.noostak.group.dto.response.retrieve.GroupsRetrieveResponse;

public interface GroupRetrieveService {
    GroupsRetrieveResponse findGroups(Long memberId);
}
