package org.noostak.group.application;

import lombok.RequiredArgsConstructor;
import org.noostak.group.dto.request.GroupCreateRequest;
import org.noostak.group.dto.response.GroupCreateInternalResponse;
import org.noostak.group.dto.response.GroupCreateResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupCreateService groupCreateService;

    public GroupCreateResponse createGroup(Long memberId, GroupCreateRequest request) throws IOException {
        GroupCreateInternalResponse response = groupCreateService.createGroup(memberId, request);
        return GroupCreateResponse.of(response.group(), response.groupProfileImageUrl());
    }
}
