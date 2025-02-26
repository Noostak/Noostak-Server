package org.noostak.appointment.application;

import org.junit.jupiter.api.*;
import org.noostak.appointment.application.create.AppointmentCreateServiceImpl;
import org.noostak.appointment.common.exception.AppointmentErrorCode;
import org.noostak.appointment.common.exception.AppointmentException;
import org.noostak.appointment.domain.*;
import org.noostak.appointment.domain.vo.AppointmentCategory;
import org.noostak.appointment.domain.vo.AppointmentStatus;
import org.noostak.appointment.dto.request.AppointmentCreateRequest;
import org.noostak.appointment.dto.request.AppointmentHostSelectionTimeRequest;
import org.noostak.group.domain.Group;
import org.noostak.group.domain.GroupRepository;
import org.noostak.group.domain.GroupRepositoryTest;
import org.noostak.group.domain.vo.GroupName;
import org.noostak.group.domain.vo.GroupProfileImageKey;
import org.noostak.member.MemberRepositoryTest;
import org.noostak.member.domain.Member;
import org.noostak.member.domain.MemberRepository;
import org.noostak.member.domain.vo.AuthId;
import org.noostak.member.domain.vo.AuthType;
import org.noostak.member.domain.vo.MemberName;
import org.noostak.member.domain.vo.MemberProfileImageKey;
import org.noostak.membergroup.MemberGroupRepositoryTest;
import org.noostak.membergroup.domain.MemberGroup;
import org.noostak.membergroup.domain.MemberGroupRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
public class AppointmentCreateServiceImplTest {

    private MemberRepository memberRepository;
    private GroupRepository groupRepository;
    private MemberGroupRepository memberGroupRepository;
    private AppointmentRepository appointmentRepository;
    private AppointmentCreateServiceImpl appointmentCreateService;
    private AppointmentHostSelectionTimeRepository appointmentHostSelectionTimeRepository;

    private Long savedMemberId;
    private Long savedGroupId;

    @BeforeEach
    void setUp() {
        initializeRepositories();
        initializeTestData();
    }

    @AfterEach
    void tearDown() {
        memberGroupRepository.deleteAll();
        groupRepository.deleteAll();
        memberRepository.deleteAll();
        appointmentRepository.deleteAll();
    }

    @Nested
    @DisplayName("성공 케이스")
    class Success {

        @Test
        @DisplayName("정상적으로 약속을 생성할 수 있다.")
        void shouldCreateAppointmentSuccessfully() {
            // Given
            List<AppointmentHostSelectionTimeRequest> selectionTimes = createSelectionTimes();
            AppointmentCreateRequest request = AppointmentCreateRequest.of("회의", "중요", 60L, selectionTimes);

            // When
            appointmentCreateService.createAppointment(savedMemberId, savedGroupId, request);

            // Then
            List<Appointment> appointments = appointmentRepository.findAll();
            assertThat(appointments).hasSize(1);
            assertThat(appointments.getFirst().getGroup().getId()).isEqualTo(savedGroupId);
            assertThat(appointments.getFirst().getAppointmentHostId()).isEqualTo(savedMemberId);
            assertThat(appointments.getFirst().getDuration().value()).isEqualTo(request.duration());
            assertThat(appointments.getFirst().getCategory()).isEqualTo(AppointmentCategory.from(request.category()));
            assertThat(appointments.getFirst().getAppointmentStatus()).isEqualTo(AppointmentStatus.PROGRESS);
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class Failure {

        @Test
        @DisplayName("그룹에 속하지 않은 멤버가 약속을 생성하면 예외 발생")
        void shouldThrowExceptionWhenMemberNotInGroup() {
            Long invalidMemberId = 999L;
            AppointmentCreateRequest request = AppointmentCreateRequest.of("회의", "일정", 60L, List.of());

            assertThatThrownBy(() -> appointmentCreateService.createAppointment(invalidMemberId, savedGroupId, request))
                    .isInstanceOf(AppointmentException.class)
                    .hasMessage(AppointmentErrorCode.MEMBER_NOT_IN_GROUP.getMessage());
        }

        @Test
        @DisplayName("존재하지 않는 그룹에서 약속을 생성하면 예외 발생")
        void shouldThrowExceptionWhenGroupDoesNotExist() {
            Long nonExistentGroupId = 9999L;
            AppointmentCreateRequest request = AppointmentCreateRequest.of("회의", "취미", 60L, List.of());

            assertThatThrownBy(() -> appointmentCreateService.createAppointment(savedMemberId, nonExistentGroupId, request))
                    .isInstanceOf(AppointmentException.class)
                    .hasMessage(AppointmentErrorCode.MEMBER_NOT_IN_GROUP.getMessage());
        }
    }

    private void initializeRepositories() {
        memberRepository = new MemberRepositoryTest();
        groupRepository = new GroupRepositoryTest();
        memberGroupRepository = new MemberGroupRepositoryTest();
        appointmentRepository = new AppointmentRepositoryTest();
        appointmentHostSelectionTimeRepository = new AppointmentHostSelectionTimeRepositoryTest();
        appointmentCreateService = new AppointmentCreateServiceImpl(memberGroupRepository, groupRepository, appointmentRepository, appointmentHostSelectionTimeRepository);
    }

    private void initializeTestData() {
        savedMemberId = createAndSaveMember("사용자One", "profilekeyOne", "authid12", "refresh-token1");
        savedGroupId = createAndSaveGroup(savedMemberId, "테스트그룹", "group-image/1", "INVITE");
        saveMemberGroup(savedMemberId, savedGroupId);
    }

    private List<AppointmentHostSelectionTimeRequest> createSelectionTimes() {
        return List.of(
                AppointmentHostSelectionTimeRequest.of(
                        LocalDateTime.of(2024, 3, 15, 10, 0),
                        LocalDateTime.of(2024, 3, 15, 10, 30),
                        LocalDateTime.of(2024, 3, 15, 11, 0)
                )
        );
    }

    private Long createAndSaveMember(String name, String profileKey, String authId, String refreshToken) {
        return memberRepository.save(Member.of(
                MemberName.from(name),
                MemberProfileImageKey.from(profileKey),
                AuthType.GOOGLE,
                AuthId.from(authId),
                refreshToken
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
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new AppointmentException(AppointmentErrorCode.GROUP_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AppointmentException(AppointmentErrorCode.MEMBER_NOT_IN_GROUP));

        memberGroupRepository.save(MemberGroup.of(member, group));
    }
}
