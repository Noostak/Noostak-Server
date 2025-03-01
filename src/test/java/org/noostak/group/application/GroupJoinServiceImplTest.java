package org.noostak.group.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.noostak.group.common.exception.GroupErrorCode;
import org.noostak.group.common.exception.GroupException;
import org.noostak.group.domain.Group;
import org.noostak.group.domain.GroupRepository;
import org.noostak.group.domain.GroupRepositoryTest;
import org.noostak.group.domain.vo.GroupInvitationCode;
import org.noostak.group.domain.vo.GroupName;
import org.noostak.group.domain.vo.GroupProfileImageKey;
import org.noostak.group.dto.response.GroupJoinResponse;
import org.noostak.member.MemberRepositoryTest;
import org.noostak.member.common.exception.MemberErrorCode;
import org.noostak.member.common.exception.MemberException;
import org.noostak.member.domain.Member;
import org.noostak.member.domain.MemberRepository;
import org.noostak.member.domain.vo.MemberName;
import org.noostak.member.domain.vo.MemberProfileImageKey;
import org.noostak.membergroup.MemberGroupRepositoryTest;
import org.noostak.membergroup.domain.MemberGroupRepository;

import java.io.IOException;

import static org.assertj.core.api.Assertions.*;

class GroupJoinServiceImplTest {

    private GroupRepository groupRepository;
    private MemberRepository memberRepository;
    private MemberGroupRepository memberGroupRepository;
    private GroupJoinServiceImpl groupJoinService;

    private Long savedMemberId;
    private Long savedGroupId;

    private GroupInvitationCode savedGroupInvitationCode;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);

        groupRepository = new GroupRepositoryTest();
        memberRepository = new MemberRepositoryTest();
        memberGroupRepository = new MemberGroupRepositoryTest();

        groupJoinService = new GroupJoinServiceImpl(groupRepository, memberGroupRepository,memberRepository);

        groupRepository.deleteAll();
        memberRepository.deleteAll();
        savedGroupInvitationCode = GroupInvitationCode.from("ABC123");

        Member savedMember = saveMember("jsoonworld", "key");
        savedMemberId = savedMember.getId();

        Group savedGroup = saveGroup("group","key");
        savedGroupId = savedGroup.getId();

    }

    @Nested
    @DisplayName("성공 케이스")
    class Success {

        @Test
        @DisplayName("그룹 참여 성공")
        void shouldJoinGroup() {
            // given

            // when
            GroupJoinResponse createdGroup = groupJoinService.join(savedMemberId, savedGroupInvitationCode.value());

            // then
            assertThat(createdGroup).isNotNull();
            assertThat(createdGroup.getGroupId()).isEqualTo(String.valueOf(savedGroupId));
            assertThat(memberRepository.findById(savedMemberId)).isPresent();
            assertThat(groupRepository.findByCode(savedGroupInvitationCode)).isPresent();
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class Failure {

        @Test
        @DisplayName("그룹 참여 실패 - 존재하지 않는 사용자")
        void shouldFailToJoinGroupWhenUserDoesNotExist() {
            // given
            Member fakeMember = Member.of(MemberName.from("fake"),MemberProfileImageKey.from("fake"));

            // when & then
            assertThat(memberRepository.findById(fakeMember.getId())).isEmpty();
        }

        @Test
        @DisplayName("그룹 참여 실패 - 존재하지 않는 코드")
        void shouldFailToJoinGroupWhenCodeDoesNotExist() {
            // given
            GroupInvitationCode fakeCode = GroupInvitationCode.from("FAKEKO");

            // when & then
            assertThat(memberRepository.findById(savedMemberId)).isPresent();
            assertThatThrownBy(()->groupJoinService.join(savedMemberId, fakeCode.value()))
                    .isInstanceOf(GroupException.class)
                    .hasMessage(GroupErrorCode.GROUP_NOT_FOUND_BY_CODE.getMessage());
        }
    }

    private Member saveMember(String name, String key) {
        return memberRepository.save(
                Member.of(
                        MemberName.from(name),
                        MemberProfileImageKey.from(key)
                )
        );
    }

    private Group saveGroup(String name, String key) {
        return groupRepository.save(
                Group.of(
                        1L,
                        GroupName.from(name),
                        GroupProfileImageKey.from(key),
                        savedGroupInvitationCode.value()
                )
        );
    }
}
