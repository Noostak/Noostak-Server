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
import org.noostak.infra.KeyAndUrl;
import org.noostak.infra.S3DirectoryPath;
import org.noostak.infra.S3Service;
import org.noostak.member.domain.Member;
import org.noostak.member.domain.MemberRepository;
import org.springframework.context.annotation.Primary;
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
    private final InvitationCodeGenerator invitationCodeGenerator;
    private final S3Service s3Service;

    @Override
    @Transactional
    public GroupCreateInternalResponse createGroup(Long memberId, GroupCreateRequest request) {
        Member groupHost = findGroupHost(memberId);
        KeyAndUrl response = uploadGroupProfileImage(request.file());
        Group group = createGroup(groupHost, request.groupName(), response.getKey());
        groupRepository.save(group);
        return GroupCreateInternalResponse.of(group, response.getUrl());
    }

    private Member findGroupHost(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new GroupException(GroupErrorCode.HOST_MEMBER_NOT_FOUND));
    }

    private Group createGroup(Member groupHost, String groupName, String profileImageKey) {
        GroupInvitationCode invitationCode = invitationCodeGenerator.generate();
        return Group.of(
                groupHost.getMemberId(),
                GroupName.from(groupName),
                GroupProfileImageKey.from(profileImageKey),
                invitationCode.value()
        );
    }

    private KeyAndUrl uploadGroupProfileImage(MultipartFile file) {
        try {
            return s3Service.uploadImage(S3DirectoryPath.GROUP, file);
        } catch (IOException e) {
            throw new GroupException(GroupErrorCode.GROUP_PROFILE_IMAGE_UPLOAD_FAILED);
        }
    }
}
