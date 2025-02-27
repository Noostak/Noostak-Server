package org.noostak.group.application.info;

import org.noostak.group.dto.response.info.GroupInfoResponse;

public interface GroupInfoService {
    GroupInfoResponse getGroupInfo(Long memberId, Long groupId);
}
