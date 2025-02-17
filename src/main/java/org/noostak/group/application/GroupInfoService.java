package org.noostak.group.application;

import org.noostak.group.dto.response.info.GroupInfoResponse;

public interface GroupInfoService {
    GroupInfoResponse getGroupInfo(Long memberId, Long groupId);
}
