package org.noostak.global.config.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.noostak.global.success.SuccessResponse;
import org.noostak.group.dto.request.GroupCreateRequest;
import org.noostak.group.dto.request.GroupJoinRequest;
import org.noostak.group.dto.response.GroupJoinResponse;
import org.noostak.group.dto.response.confirmed.GroupConfirmedAppointmentsResponse;
import org.noostak.group.dto.response.create.GroupCreateResponse;
import org.noostak.group.dto.response.info.GroupInfoResponse;
import org.noostak.group.dto.response.ongoing.GroupOngoingAppointmentsResponse;
import org.noostak.group.dto.response.retrieve.GroupsRetrieveResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Tag(name = "Group", description = "그룹 관련 API")
@RequestMapping("/api/v1/groups")
public interface GroupSwagger {

    @Operation(summary = "그룹 생성", description = "새로운 그룹을 생성하는 API")
    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<SuccessResponse<GroupCreateResponse>> createGroup(
            @Parameter(description = "멤버 ID", example = "1")
            @RequestAttribute Long memberId,

            @ModelAttribute GroupCreateRequest request
    ) throws IOException;

    @Operation(summary = "그룹 조회", description = "그룹 조회 API")
    @GetMapping
    ResponseEntity<SuccessResponse<GroupsRetrieveResponse>> getGroups(
            @Parameter(description = "멤버 ID")
            @RequestAttribute Long memberId
    );

    @Operation(summary = "그룹 정보 조회", description = "특정 그룹의 정보를 조회하는 API")
    @GetMapping("/{groupId}/members")
    ResponseEntity<SuccessResponse<GroupInfoResponse>> getGroupInfo(
            @Parameter(description = "멤버 ID", example = "1")
            @RequestAttribute Long memberId,

            @Parameter(description = "그룹 ID", example = "10")
            @PathVariable Long groupId
    );

    @Operation(summary = "진행 중인 약속 조회", description = "그룹의 진행 중인 약속을 조회하는 API")
    @GetMapping("/{groupId}/appointments/ongoing")
    ResponseEntity<SuccessResponse<GroupOngoingAppointmentsResponse>> getGroupOngoingAppointments(
            @Parameter(description = "멤버 ID", example = "1")
            @RequestAttribute Long memberId,

            @Parameter(description = "그룹 ID", example = "10")
            @PathVariable Long groupId
    );

    @Operation(summary = "그룹 가입", description = "새로운 그룹에 가입하는 API")
    @PostMapping("/join")
    ResponseEntity<SuccessResponse<GroupJoinResponse>> joinGroup(
            @Parameter(description = "멤버 ID", example = "1")
            @RequestAttribute Long memberId,

            @RequestBody GroupJoinRequest request
    );

    @Operation(summary = "확정된 약속 조회", description = "확정된 약속 목록을 조회하는 API")
    @GetMapping("/{groupId}/appointments/confirmed")
    ResponseEntity<SuccessResponse<GroupConfirmedAppointmentsResponse>> getGroupConfirmedAppointments(
            @Parameter(description = "멤버 ID", example = "1")
            @RequestAttribute Long memberId,

            @Parameter(description = "그룹 ID", example = "10")
            @PathVariable Long groupId
    );
}
