package org.noostak.group.application;

import org.noostak.group.dto.request.GroupCreateRequest;
import org.noostak.group.dto.response.create.GroupCreateInternalResponse;

import java.io.IOException;

public interface GroupCreateService {

    GroupCreateInternalResponse createGroup(Long memberId, GroupCreateRequest createGroupRequest) throws IOException;
}
