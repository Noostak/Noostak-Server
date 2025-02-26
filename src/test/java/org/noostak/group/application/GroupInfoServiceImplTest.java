package org.noostak.group.application;

import org.junit.jupiter.api.*;
import org.noostak.group.common.exception.GroupErrorCode;
import org.noostak.group.common.exception.GroupException;
import org.noostak.group.domain.Group;
import org.noostak.group.domain.GroupRepository;
import org.noostak.group.domain.GroupRepositoryTest;
import org.noostak.group.domain.vo.GroupName;
import org.noostak.group.domain.vo.GroupProfileImageKey;
import org.noostak.group.dto.response.info.GroupInfoResponse;
import org.noostak.infra.S3Service;
import org.noostak.member.MemberRepositoryTest;
import org.noostak.member.domain.Member;
import org.noostak.member.domain.MemberRepository;
import org.noostak.member.domain.vo.MemberName;
import org.noostak.member.domain.vo.MemberProfileImageKey;
import org.noostak.membergroup.MemberGroupRepositoryTest;
import org.noostak.membergroup.domain.MemberGroup;
import org.noostak.membergroup.domain.MemberGroupRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.mock;

public class GroupInfoServiceImplTest {

    private MemberRepository memberRepository;
    private GroupRepository groupRepository;
    private MemberGroupRepository memberGroupRepository;
    private GroupInfoService groupInfoService;
    private final S3Service s3Service = mock(S3Service.class);

    private final List<Member> members = new ArrayList<>();
    private final List<Group> groups = new ArrayList<>();

    private Long savedMemberId;
    private Long savedGroupId;

    @BeforeEach
    void setUp() {
        initializeRepositories();
        initializeMembers();
        initializeGroups();
        assignMembersToGroups();
        saveMemberGroup(members.get(1).getId(), savedGroupId);
    }

    @AfterEach
    void tearDown() {
        memberGroupRepository.deleteAll();
        groupRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Nested
    @DisplayName("성공 케이스")
    class Success {

        @Test
        @DisplayName("그룹 호스트가 정보를 조회할 때 정상적으로 반환된다.")
        void shouldRetrieveGroupInfoAsHostSuccessfully() {
            GroupInfoResponse response = groupInfoService.getGroupInfo(savedMemberId, savedGroupId);

            assertThat(response).isNotNull();
            assertThat(response.groupInfo().groupName()).isEqualTo("그룹1");
        }

        @Test
        @DisplayName("그룹 멤버가 정보를 조회할 때 정상적으로 반환된다.")
        void shouldRetrieveGroupInfoAsMemberSuccessfully() {
            Long anotherMemberId = members.get(1).getId();
            GroupInfoResponse response = groupInfoService.getGroupInfo(anotherMemberId, savedGroupId);

            assertThat(response).isNotNull();
            assertThat(response.groupInfo().groupName()).isEqualTo("그룹1");
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class Failure {

        @Test
        @DisplayName("존재하지 않는 그룹을 조회할 경우 예외 발생")
        void shouldThrowExceptionWhenGroupDoesNotExist() {
            Long nonExistentGroupId = 9999L;

            assertThatThrownBy(() -> groupInfoService.getGroupInfo(savedMemberId, nonExistentGroupId))
                    .isInstanceOf(GroupException.class)
                    .hasMessage(GroupErrorCode.GROUP_NOT_FOUND.getMessage());
        }

        @Test
        @DisplayName("그룹에 속하지 않은 멤버가 조회할 경우 예외 발생")
        void shouldThrowExceptionWhenMemberNotInGroup() {
            Long invalidMemberId = 999L;

            assertThatThrownBy(() -> groupInfoService.getGroupInfo(invalidMemberId, savedGroupId))
                    .isInstanceOf(GroupException.class)
                    .hasMessage(GroupErrorCode.MEMBER_NOT_FOUND.getMessage());
        }
    }

    private void initializeRepositories() {
        memberRepository = new MemberRepositoryTest();
        groupRepository = new GroupRepositoryTest();
        memberGroupRepository = new MemberGroupRepositoryTest();
        groupInfoService = new GroupInfoServiceImpl(groupRepository, memberGroupRepository, s3Service);
    }

    private void initializeMembers() {
        members.clear();
        IntStream.rangeClosed(1, 10)
                .mapToObj(i -> createAndSaveMember("장순" + (char) ('가' + i), "key" + i, "authId" + i, "refreshToken" + i))
                .map(memberId -> memberRepository.findById(memberId).orElseThrow())
                .forEach(members::add);
        savedMemberId = members.get(0).getId();
    }

    private void initializeGroups() {
        groups.clear();
        IntStream.rangeClosed(1, 5)
                .mapToObj(i -> createAndSaveGroup(savedMemberId, "그룹" + i, "group-images/" + i, "INVIT" + i))
                .map(groupId -> groupRepository.findById(groupId).orElseThrow())
                .forEach(groups::add);
        savedGroupId = groups.get(0).getId();
    }

    private Long createAndSaveMember(String name, String key, String authId, String refreshToken) {
        return memberRepository.save(Member.of(
                MemberName.from(name),
                MemberProfileImageKey.from(key)
        )).getId();
    }

    private Long createAndSaveGroup(Long groupHostId, String groupName, String groupImageUrl, String inviteCode) {
        return groupRepository.save(Group.of(
                groupHostId,
                GroupName.from(groupName),
                GroupProfileImageKey.from(groupImageUrl),
                inviteCode
        )).getId();
    }

    private void saveMemberGroup(Long memberId, Long groupId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GroupException(GroupErrorCode.GROUP_MEMBER_NOT_FOUND));
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupException(GroupErrorCode.GROUP_NOT_FOUND));

        memberGroupRepository.save(MemberGroup.of(member, group));
    }

    private void assignMembersToGroups() {
        members.forEach(member ->
                groups.stream()
                        .filter(group -> (members.indexOf(member) + groups.indexOf(group)) % 2 == 0 || members.indexOf(member) < 2)
                        .forEach(group -> saveMemberGroup(member.getId(), group.getId()))
        );
    }
}
