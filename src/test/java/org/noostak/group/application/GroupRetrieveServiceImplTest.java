package org.noostak.group.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.noostak.group.common.exception.GroupErrorCode;
import org.noostak.group.common.exception.GroupException;
import org.noostak.group.domain.Group;
import org.noostak.group.domain.GroupRepository;
import org.noostak.group.domain.GroupRepositoryTest;
import org.noostak.group.domain.vo.GroupName;
import org.noostak.group.domain.vo.GroupProfileImageKey;
import org.noostak.group.dto.response.retrieve.GroupsRetrieveResponse;
import org.noostak.infra.S3Service;
import org.noostak.member.MemberRepositoryTest;
import org.noostak.member.domain.Member;
import org.noostak.member.domain.MemberRepository;
import org.noostak.member.domain.vo.MemberName;
import org.noostak.member.domain.vo.MemberProfileImageKey;
import org.noostak.membergroup.MemberGroupRepositoryTest;
import org.noostak.membergroup.domain.MemberGroup;
import org.noostak.membergroup.domain.MemberGroupRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;

class GroupRetrieveServiceImplTest {

    private MemberRepository memberRepository;
    private GroupRepository groupRepository;
    private MemberGroupRepository memberGroupRepository;
    private GroupRetrieveServiceImpl groupRetrieveService;
    private final S3Service s3Service = mock(S3Service.class);

    private Long savedMemberId;
    private Long savedGroup1Id;
    private Long savedGroup2Id;
    private Long savedGroup3Id;

    @BeforeEach
    void setUp() {
        memberRepository = new MemberRepositoryTest();
        memberGroupRepository = new MemberGroupRepositoryTest();
        groupRepository = new GroupRepositoryTest();
        memberGroupRepository.deleteAll();

        groupRetrieveService = new GroupRetrieveServiceImpl(memberGroupRepository, s3Service);

        Member savedMember = saveMember("MemberOne", "key1");
        savedMemberId = savedMember.getId();

        Group savedGroup1 = saveGroup(savedMemberId, "StudyGroup", "group-images/1", "ABC123");
        Group savedGroup2 = saveGroup(savedMemberId, "GamingClub", "group-images/2", "XYZ789");
        Group savedGroup3 = saveGroup(savedMemberId, "BookClub", "group-images/3", "LMN456");
        savedGroup1Id = savedGroup1.getId();
        savedGroup2Id = savedGroup2.getId();
        savedGroup3Id = savedGroup3.getId();

        saveMemberGroup(savedMemberId, savedGroup1Id);
        saveMemberGroup(savedMemberId, savedGroup2Id);
        saveMemberGroup(savedMemberId, savedGroup3Id);
    }


    @Nested
    @DisplayName("성공 케이스")
    class Success {

        @Test
        @DisplayName("멤버가 여러 개의 그룹을 정상적으로 조회한다.")
        void shouldRetrieveMultipleGroupsSuccessfully() {
            // given
            Long memberId = savedMemberId;

            List<MemberGroup> memberGroups = memberGroupRepository.findByMember_MemberId(memberId);
            assertThat(memberGroups).isNotEmpty();

            // when
            GroupsRetrieveResponse response = groupRetrieveService.findGroups(memberId);

            // then
            assertThat(response).isNotNull();
            assertThat(response.groups()).hasSize(3);
        }

        @Test
        @DisplayName("멤버가 하나의 그룹만 속해 있는 경우 정상 조회")
        void shouldRetrieveSingleGroupSuccessfully() {
            // given
            Long memberId = saveMember("singleUser", "keySingle").getId();
            Long groupId = saveGroup(memberId, "SingleGroup", "group-images/single", "SINGLE").getId();

            saveMemberGroup(memberId, groupId);

            // when
            GroupsRetrieveResponse response = groupRetrieveService.findGroups(memberId);

            // then
            assertThat(response).isNotNull();
            assertThat(response.groups()).hasSize(1);
        }

        @Test
        @DisplayName("여러 명의 멤버가 같은 그룹에 속한 경우 특정 멤버가 정상적으로 조회")
        void shouldRetrieveGroupsWhenMultipleMembersInSameGroup() {
            // given

            Long member1 = saveMember("memberOne", "key1").getId();
            Long member2 = saveMember("memberTwo", "key2").getId();
            Long groupId = saveGroup(member1, "SharedGroup", "group-images/shared", "SHARED").getId();

            saveMemberGroup(member1, groupId);
            saveMemberGroup(member2, groupId);

            // when
            GroupsRetrieveResponse response1 = groupRetrieveService.findGroups(member1);
            GroupsRetrieveResponse response2 = groupRetrieveService.findGroups(member2);

            // then
            assertThat(response1.groups()).hasSize(1);
            assertThat(response2.groups()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class Failure {

        @Test
        @DisplayName("멤버가 속한 그룹이 없는 경우 예외를 발생시킨다.")
        void shouldThrowExceptionWhenNoGroupsFound() {
            // given
            Long invalidMemberId = 999L;

            // when & then
            assertThatThrownBy(() -> groupRetrieveService.findGroups(invalidMemberId))
                    .isInstanceOf(GroupException.class)
                    .hasMessage(GroupErrorCode.GROUP_NOT_FOUND.getMessage());
        }

        @Test
        @DisplayName("그룹이 삭제된 후 멤버가 속한 그룹을 조회하면 예외 발생")
        void shouldThrowExceptionWhenGroupIsDeleted() {
            // given
            Long memberId = savedMemberId;
            memberGroupRepository.deleteAll();

            // when & then
            assertThatThrownBy(() -> groupRetrieveService.findGroups(memberId))
                    .isInstanceOf(GroupException.class)
                    .hasMessage(GroupErrorCode.GROUP_NOT_FOUND.getMessage());
        }

        @Test
        @DisplayName("멤버 ID가 null이면 GroupException 발생")
        void shouldThrowExceptionWhenMemberIdIsNull() {
            // when & then
            assertThatThrownBy(() -> groupRetrieveService.findGroups(null))
                    .isInstanceOf(GroupException.class)
                    .hasMessage(GroupErrorCode.GROUP_NOT_FOUND.getMessage());
        }
    }

    private Group saveGroup(Long groupHostId, String groupName, String groupImageUrl, String inviteCode) {
        return groupRepository.save(
                Group.of(
                        groupHostId,
                        GroupName.from(groupName),
                        GroupProfileImageKey.from(groupImageUrl),
                        inviteCode
                )
        );
    }

    private Member saveMember(String name, String key) {
        return memberRepository.save(
                Member.of(
                        MemberName.from(name),
                        MemberProfileImageKey.from(key)
                )
        );
    }

    private MemberGroup saveMemberGroup(Long memberId, Long groupId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GroupException(GroupErrorCode.GROUP_MEMBER_NOT_FOUND));

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupException(GroupErrorCode.GROUP_NOT_FOUND));

        MemberGroup memberGroup = MemberGroup.of(member, group);
        MemberGroup savedMemberGroup = memberGroupRepository.save(memberGroup);

        return savedMemberGroup;
    }
}
