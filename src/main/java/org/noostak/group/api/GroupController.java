package org.noostak.group.api;

import lombok.RequiredArgsConstructor;
import org.noostak.global.success.SuccessResponse;
import org.noostak.group.application.GroupService;
import org.noostak.group.dto.request.GroupCreateRequest;
import org.noostak.group.dto.response.GroupCreateResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static org.noostak.group.common.success.GroupSuccessCode.GROUP_CREATED;

@RestController
@RequestMapping("/api/v1/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<SuccessResponse<GroupCreateResponse>> createGroup(
            // @AuthenticationPrincipal Long memberId,
            @ModelAttribute GroupCreateRequest request
    ) throws IOException {
        // TODO: AuthenticationPrincipal
        Long memberId = 1L;
        GroupCreateResponse response = groupService.createGroup(memberId, request);
        return ResponseEntity.ok((SuccessResponse.of(GROUP_CREATED, response)));
    }
}
