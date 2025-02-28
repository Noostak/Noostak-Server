package org.noostak.group.application;

import org.noostak.group.dto.response.GroupJoinResponse;

public interface GroupJoinService {

    GroupJoinResponse join(Long memberId, String inviteCode);
}
