package org.noostak.group.application;

import lombok.RequiredArgsConstructor;
import org.noostak.group.common.exception.GroupErrorCode;
import org.noostak.group.common.exception.GroupException;
import org.noostak.group.domain.GroupRepository;
import org.noostak.group.domain.Group;
import org.noostak.group.domain.vo.GroupInvitationCode;
import org.noostak.group.domain.vo.GroupName;
import org.noostak.group.domain.vo.GroupProfileImageKey;
import org.noostak.group.dto.request.GroupCreateRequest;
import org.noostak.group.dto.response.GroupCreateInternalResponse;
import org.noostak.infra.S3Service;
import org.noostak.member.domain.Member;
import org.noostak.member.domain.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupCreateServiceImpl implements GroupCreateService {

    private final MemberRepository memberRepository;
    private final GroupRepository groupRepository;
    private final InvitationCodeGenerator generator;
    private final S3Service s3Service;

    @Override
    @Transactional
    public GroupCreateInternalResponse createGroup(Long memberId, GroupCreateRequest request) {
        Member groupHost = findGroupHost(memberId);
        Group group = createGroup(groupHost, request.groupName());
        groupRepository.save(group);

        GroupProfileImageUploadResponse response = uploadGroupProfileImage(request.file());
        updateGroupWithProfileImage(group, response);

        return GroupCreateInternalResponse.of(group, response.getUrl());
    }

    private Member findGroupHost(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new GroupException(GroupErrorCode.HOST_MEMBER_NOT_FOUND));
    }

    private Group createGroup(Member groupHost, String groupName) {
        GroupInvitationCode invitationCode = generateInvitationCode();
        return Group.of(
                groupHost.getMemberId(),
                GroupName.from(groupName),
                null,
                invitationCode.value()
        );
    }

    private GroupInvitationCode generateInvitationCode() {
        return generator.generate();
    }

    private GroupProfileImageUploadResponse uploadGroupProfileImage(MultipartFile file) {
        try {
            return s3Service.uploadGroupProfileImage(file);
        } catch (IOException e) {
            throw new GroupException(GroupErrorCode.GROUP_PROFILE_IMAGE_UPLOAD_FAILED);
        }
    }

    private void updateGroupWithProfileImage(Group group, GroupProfileImageUploadResponse response) {
        group.updateProfileImageKey(GroupProfileImageKey.of(response.getKey()));
        groupRepository.save(group);
    }
}
