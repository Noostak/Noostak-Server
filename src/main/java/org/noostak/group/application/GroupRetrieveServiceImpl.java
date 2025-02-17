package org.noostak.group.application;

import lombok.RequiredArgsConstructor;
import org.noostak.group.common.exception.GroupErrorCode;
import org.noostak.group.common.exception.GroupException;
import org.noostak.group.domain.Group;
import org.noostak.group.dto.response.retrieve.GroupRetrieveResponse;
import org.noostak.group.dto.response.retrieve.GroupsRetrieveResponse;
import org.noostak.infra.S3Service;
import org.noostak.membergroup.domain.MemberGroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupRetrieveServiceImpl implements GroupRetrieveService {

    private final MemberGroupRepository memberGroupRepository;
    private final S3Service s3Service;

    @Override
    public GroupsRetrieveResponse findGroups(Long memberId) {
        List<Group> groups = getGroupsByMemberId(memberId);
        validateGroups(groups);
        return convertToResponse(groups);
    }

    private List<Group> getGroupsByMemberId(Long memberId) {
        return memberGroupRepository.findGroupsByMemberId(memberId);
    }

    private void validateGroups(List<Group> groups) {
        if (groups.isEmpty()) {
            throw new GroupException(GroupErrorCode.GROUP_NOT_FOUND);
        }
    }

    private GroupsRetrieveResponse convertToResponse(List<Group> groups) {
        List<GroupRetrieveResponse> groupResponses = groups.stream()
                .map(group -> GroupRetrieveResponse.of(
                        group,
                        s3Service.getImageUrl(group.getKey().value())
                ))
                .toList();
        return GroupsRetrieveResponse.of(groupResponses);
    }
}
