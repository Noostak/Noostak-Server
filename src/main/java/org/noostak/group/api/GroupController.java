package org.noostak.group.api;

import lombok.RequiredArgsConstructor;
import org.noostak.global.success.SuccessResponse;
import org.noostak.group.application.GroupService;
import org.noostak.group.dto.request.GroupCreateRequest;
import org.noostak.group.dto.request.GroupJoinRequest;
import org.noostak.group.dto.response.GroupJoinResponse;
import org.noostak.group.dto.response.create.GroupCreateResponse;
import org.noostak.group.dto.response.info.GroupInfoResponse;
import org.noostak.group.dto.response.ongoing.GroupOngoingAppointmentsResponse;
import org.noostak.group.dto.response.retrieve.GroupsRetrieveResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static org.noostak.group.common.success.GroupSuccessCode.*;

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

    @GetMapping
    public ResponseEntity<SuccessResponse<GroupsRetrieveResponse>> getGroups(
            // @AuthenticationPrincipal Long memberId
    ) {
        // TODO: AuthenticationPrincipal
        Long memberId = 1L;
        GroupsRetrieveResponse groups = groupService.findGroups(memberId);
        return ResponseEntity.ok(SuccessResponse.of(GROUP_RETRIEVED, groups));
    }

    @GetMapping("/{groupId}/members")
    public ResponseEntity<SuccessResponse<GroupInfoResponse>> getGroupInfo(
            // @AuthenticationPrincipal Long memberId,
            @PathVariable Long groupId
    ) {
        Long memberId = 1L;
        GroupInfoResponse groupInfo = groupService.getGroupInfo(memberId, groupId);
        return ResponseEntity.ok(SuccessResponse.of(GROUP_INFO, groupInfo));
    }

    @GetMapping("/{groupId}/appointments/ongoing")
    public ResponseEntity<SuccessResponse<GroupOngoingAppointmentsResponse>> getGroupOngoingAppointments(
            // @AuthenticationPrincipal Long memberId,
            @PathVariable Long groupId
    ) {
        Long memberId = 1L;
        GroupOngoingAppointmentsResponse groupOngoingAppointments = groupService.getGroupOngoingAppointments(memberId, groupId);
        return ResponseEntity.ok(SuccessResponse.of(GROUP_ONGOING_APPOINTMENTS_LOADED, groupOngoingAppointments));
    }

    @PostMapping("/join")
    public ResponseEntity<SuccessResponse<GroupJoinResponse>> joinGroup(
            @RequestAttribute Long memberId,
            @RequestBody GroupJoinRequest request
            ){
        String groupInviteCode = request.getGroupInviteCode();
        GroupJoinResponse response = groupService.joinGroup(memberId, groupInviteCode);
        return ResponseEntity.ok(SuccessResponse.of(GROUP_JOINED,response));
    }
}
