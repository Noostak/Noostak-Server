package org.noostak.group.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.noostak.group.common.exception.GroupErrorCode;
import org.noostak.group.common.exception.GroupException;
import org.noostak.group.domain.GroupRepository;
import org.noostak.group.domain.vo.GroupInvitationCode;
import org.noostak.group.domain.vo.GroupName;
import org.noostak.group.dto.request.GroupCreateRequest;
import org.noostak.group.dto.response.create.GroupCreateInternalResponse;
import org.noostak.infra.KeyAndUrl;
import org.noostak.infra.S3DirectoryPath;
import org.noostak.infra.S3Service;
import org.noostak.member.domain.Member;
import org.noostak.member.domain.MemberRepository;
import org.noostak.member.domain.vo.AuthId;
import org.noostak.member.domain.vo.AuthType;
import org.noostak.member.domain.vo.MemberName;
import org.noostak.member.domain.vo.MemberProfileImageKey;
import org.noostak.server.group.domain.GroupRepositoryTest;
import org.noostak.member.MemberRepositoryTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class GroupCreateServiceImplTest {

    private GroupRepository groupRepository;
    private MemberRepository memberRepository;
    private GroupCreateServiceImpl groupCreateService;
    private Long savedMemberId;

    private final S3Service s3Service = mock(S3Service.class);
    private MockMultipartFile file;

    @Mock
    private InvitationCodeGenerator invitationCodeGenerator;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);

        groupRepository = new GroupRepositoryTest();
        memberRepository = new MemberRepositoryTest();

        groupCreateService = new GroupCreateServiceImpl(memberRepository, groupRepository, invitationCodeGenerator, s3Service);

        groupRepository.deleteAll();
        memberRepository.deleteAll();

        Member savedMember = saveMember("jsoonworld", "key", "123456", "refreshToken1");
        savedMemberId = savedMember.getMemberId();

        Mockito.when(invitationCodeGenerator.generate()).thenReturn(GroupInvitationCode.from("ABC123"));

        file = new MockMultipartFile(
                "file",
                "test-image.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test image content".getBytes()
        );

        when(s3Service.uploadImage(eq(S3DirectoryPath.GROUP), any(MultipartFile.class)))
                .thenReturn(KeyAndUrl.of("uploaded-image-key", "https://fake-s3-url.com/uploaded-image.jpg"));
    }

    @Nested
    @DisplayName("성공 케이스")
    class Success {

        @Test
        @DisplayName("그룹 생성 성공")
        void shouldCreateGroup() {
            // given
            String groupName = "TestGroup";
            GroupCreateRequest request = GroupCreateRequest.of(groupName, file);

            // when
            GroupCreateInternalResponse createdGroup = groupCreateService.createGroup(savedMemberId, request);

            // then
            assertThat(createdGroup).isNotNull();
            assertThat(createdGroup.group().getCode()).isEqualTo(GroupInvitationCode.from("ABC123"));
            assertThat(groupRepository.findById(createdGroup.group().getGroupHostId())).isPresent();
            assertThat(createdGroup.groupProfileImageUrl()).isEqualTo("https://fake-s3-url.com/uploaded-image.jpg");
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class Failure {

        @Test
        @DisplayName("그룹 생성 실패 - 존재하지 않는 사용자")
        void shouldFailToCreateGroupWhenUserDoesNotExist() {
            // given
            Long invalidUserId = 999L;
            GroupName invalidGroupName = GroupName.from("InvalidGroup");
            GroupCreateRequest request = GroupCreateRequest.of(invalidGroupName.value(), file);

            // when & then
            assertThatThrownBy(() -> groupCreateService.createGroup(invalidUserId, request))
                    .isInstanceOf(GroupException.class)
                    .hasMessage(GroupErrorCode.HOST_MEMBER_NOT_FOUND.getMessage());
        }
    }

    private Member saveMember(String name, String key, String authId, String refreshToken) {
        return memberRepository.save(
                Member.of(
                        MemberName.from(name),
                        MemberProfileImageKey.from(key),
                        AuthType.GOOGLE,
                        AuthId.from(authId),
                        refreshToken
                )
        );
    }
}
