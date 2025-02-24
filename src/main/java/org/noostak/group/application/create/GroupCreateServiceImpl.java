package org.noostak.group.application.create;

import lombok.RequiredArgsConstructor;
import org.noostak.group.application.InvitationCodeGenerator;
import org.noostak.group.common.exception.GroupErrorCode;
import org.noostak.group.common.exception.GroupException;
import org.noostak.group.domain.GroupRepository;
import org.noostak.group.domain.Group;
import org.noostak.group.domain.vo.GroupInvitationCode;
import org.noostak.group.domain.vo.GroupName;
import org.noostak.group.domain.vo.GroupProfileImageKey;
import org.noostak.group.dto.request.GroupCreateRequest;
import org.noostak.group.dto.response.create.GroupCreateInternalResponse;
import org.noostak.infra.KeyAndUrl;
import org.noostak.infra.S3DirectoryPath;
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
    private final InvitationCodeGenerator invitationCodeGenerator;
    private final S3Service s3Service;

    @Override
    @Transactional
    public GroupCreateInternalResponse createGroup(Long memberId, GroupCreateRequest request) {
        Member groupHost = findGroupHost(memberId);
        KeyAndUrl response = uploadGroupProfileImageSafely(request.file());

        Group group = createGroup(groupHost, request.groupName(), response.getKey());

        return saveGroup(group, response.getUrl(), response.getKey());
    }

    private Member findGroupHost(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new GroupException(GroupErrorCode.HOST_MEMBER_NOT_FOUND));
    }

    private Group createGroup(Member groupHost, String groupName, String profileImageKey) {
        GroupInvitationCode invitationCode = invitationCodeGenerator.generate();
        return Group.of(
                groupHost.getId(),
                GroupName.from(groupName),
                GroupProfileImageKey.from(profileImageKey),
                invitationCode.value()
        );
    }

    private GroupCreateInternalResponse saveGroup(Group group, String imageUrl, String profileImageKey) {
        try {
            groupRepository.save(group);
            return GroupCreateInternalResponse.of(group, imageUrl);
        } catch (Exception e) {
            deleteUploadedImageSafely(profileImageKey);
            throw new GroupException(GroupErrorCode.GROUP_CREATION_FAILED);
        }
    }

    private KeyAndUrl uploadGroupProfileImageSafely(MultipartFile file) {
        try {
            return s3Service.uploadImage(S3DirectoryPath.GROUP, file);
        } catch (IOException e) {
            throw new GroupException(GroupErrorCode.GROUP_PROFILE_IMAGE_UPLOAD_FAILED);
        }
    }

    private void deleteUploadedImageSafely(String key) {
        try {
            s3Service.deleteImage(key);
        } catch (Exception e) {
            throw new GroupException(GroupErrorCode.GROUP_PROFILE_IMAGE_DELETE_FAILED);
        }
    }
}


