package org.noostak.group.dto.response;


import lombok.Getter;

@Getter
public class GroupJoinResponse {
    String groupId;

    private GroupJoinResponse(String groupId) {
        this.groupId = groupId;
    }

    public static GroupJoinResponse of(Long groupId){
        return new GroupJoinResponse(String.valueOf(groupId));
    }
}
