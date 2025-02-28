package org.noostak.group.application;

import lombok.RequiredArgsConstructor;
import org.noostak.group.application.confirmed.GroupConfirmedAppointmentsService;
import org.noostak.group.application.create.GroupCreateService;
import org.noostak.group.application.info.GroupInfoService;
import org.noostak.group.application.ongoing.GroupOngoingAppointmentService;
import org.noostak.group.application.retrieve.GroupRetrieveService;
import org.noostak.group.dto.request.GroupCreateRequest;
import org.noostak.group.dto.response.GroupJoinResponse;
import org.noostak.group.dto.response.confirmed.GroupConfirmedAppointmentsResponse;
import org.noostak.group.dto.response.create.GroupCreateInternalResponse;
import org.noostak.group.dto.response.create.GroupCreateResponse;
import org.noostak.group.dto.response.info.GroupInfoResponse;
import org.noostak.group.dto.response.ongoing.GroupOngoingAppointmentsResponse;
import org.noostak.group.dto.response.retrieve.GroupsRetrieveResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupCreateService groupCreateService;
    private final GroupRetrieveService groupRetrieveService;
    private final GroupInfoService groupInfoService;
    private final GroupOngoingAppointmentService groupOngoingAppointmentsService;
    private final GroupJoinService groupJoinService;
    private final GroupConfirmedAppointmentsService groupConfirmedAppointmentsService;

    public GroupCreateResponse createGroup(Long memberId, GroupCreateRequest request) throws IOException {
        GroupCreateInternalResponse response = groupCreateService.createGroup(memberId, request);
        return GroupCreateResponse.of(response.group(), response.groupProfileImageUrl());
    }

    public GroupsRetrieveResponse findGroups(Long memberId) {
        return groupRetrieveService.findGroups(memberId);
    }

    public GroupInfoResponse getGroupInfo(Long memberId, Long groupId) {
        return groupInfoService.getGroupInfo(memberId, groupId);
    }

    public GroupOngoingAppointmentsResponse getGroupOngoingAppointments(Long memberId, Long groupId) {
        return groupOngoingAppointmentsService.getGroupOngoingAppointments(memberId, groupId);
    }

    public GroupJoinResponse joinGroup(Long memberId, String inviteCode){
        return groupJoinService.join(memberId,inviteCode);
    }

    public GroupConfirmedAppointmentsResponse getGroupConfirmedAppointments(Long memberId, Long groupId) {
        return groupConfirmedAppointmentsService.getGroupConfirmedAppointments(memberId, groupId);
    }
}
