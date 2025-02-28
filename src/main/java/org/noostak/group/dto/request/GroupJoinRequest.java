package org.noostak.group.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GroupJoinRequest {
    String groupInviteCode;
}
