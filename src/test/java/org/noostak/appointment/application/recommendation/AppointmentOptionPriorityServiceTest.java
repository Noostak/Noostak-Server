package org.noostak.appointment.application.recommendation;

import org.junit.jupiter.api.*;
import org.noostak.appointment.application.recommendation.impl.AppointmentOptionPriorityServiceImpl;
import org.noostak.appointment.common.exception.AppointmentErrorCode;
import org.noostak.appointment.common.exception.AppointmentException;
import org.noostak.appointment.domain.Appointment;
import org.noostak.appointment.domain.AppointmentRepository;
import org.noostak.appointment.domain.AppointmentRepositoryTest;
import org.noostak.appointment.dto.response.recommendation.AppointmentPriorityGroupResponse;
import org.noostak.appointmentmember.domain.AppointmentMember;
import org.noostak.appointmentmember.domain.AppointmentMemberAvailableTime;
import org.noostak.appointmentmember.domain.AppointmentMemberRepository;
import org.noostak.appointmentmember.domain.repository.AppointmentMemberRepositoryTest;
import org.noostak.appointmentmember.domain.vo.AppointmentAvailability;
import org.noostak.appointmentoption.domain.AppointmentOption;
import org.noostak.group.domain.Group;
import org.noostak.group.domain.GroupRepository;
import org.noostak.group.domain.GroupRepositoryTest;
import org.noostak.likes.domain.LikeRepository;
import org.noostak.likes.domain.LikeRepositoryTest;
import org.noostak.member.MemberRepositoryTest;
import org.noostak.member.domain.*;
import org.noostak.member.domain.vo.*;
import org.noostak.membergroup.MemberGroupRepositoryTest;
import org.noostak.membergroup.domain.MemberGroup;
import org.noostak.membergroup.domain.MemberGroupRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class AppointmentOptionPriorityServiceTest {

    private AppointmentOptionPriorityService priorityService;
    private LikeRepository likeRepository;
    private GroupRepository groupRepository;
    private MemberRepository memberRepository;
    private MemberGroupRepository memberGroupRepository;
    private AppointmentRepository appointmentRepository;
    private AppointmentMemberRepository appointmentMemberRepository;

    private Long savedMemberId;
    private Long savedGroupId;

    @BeforeEach
    void setUp() {
        initializeRepositories();
        initializeTestData();
    }

    @AfterEach
    void tearDown() {
        likeRepository.deleteAll();
        groupRepository.deleteAll();
        memberRepository.deleteAll();
        memberGroupRepository.deleteAll();
    }

    @Nested
    @DisplayName("성공 케이스 검증")
    class SuccessCases {

        @Test
        @DisplayName("우선순위 그룹을 정상적으로 반환한다.")
        void shouldReturnPriorityGroupsSuccessfully() {
            // Given
            List<AppointmentOption> sortedOptions = createTestOptions();
            Map<Long, List<AppointmentMemberAvailableTime>> memberAvailability = createTestMemberAvailability();
            Map<Long, String> memberNames = Map.of(savedMemberId, "사용자One");

            // When
            List<AppointmentPriorityGroupResponse> priorityGroups = priorityService
                    .determineOptionPriority(sortedOptions, savedMemberId, memberAvailability, memberNames);

            // Then
            assertThat(priorityGroups).isNotEmpty();
            assertThat(priorityGroups.get(0).priority()).isEqualTo(1L);
            assertThat(priorityGroups.get(0).options()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("실패 케이스 검증")
    class FailureCases {

        @Test
        @DisplayName("빈 옵션 리스트가 주어질 경우 빈 리스트 반환")
        void shouldReturnEmptyListWhenOptionsAreEmpty() {
            // Given
            List<AppointmentOption> sortedOptions = List.of();
            Map<Long, List<AppointmentMemberAvailableTime>> memberAvailability = createTestMemberAvailability();
            Map<Long, String> memberNames = Map.of(savedMemberId, "사용자One");

            // When
            List<AppointmentPriorityGroupResponse> priorityGroups = priorityService
                    .determineOptionPriority(sortedOptions, savedMemberId, memberAvailability, memberNames);

            // Then
            assertThat(priorityGroups).isEmpty();
        }
    }

    private void initializeRepositories() {
        likeRepository = new LikeRepositoryTest();
        groupRepository = new GroupRepositoryTest();
        memberRepository = new MemberRepositoryTest();
        memberGroupRepository = new MemberGroupRepositoryTest();
        priorityService = new AppointmentOptionPriorityServiceImpl(likeRepository, groupRepository);
        appointmentRepository = new AppointmentRepositoryTest();
        appointmentMemberRepository = new AppointmentMemberRepositoryTest();
    }

    private void initializeTestData() {
        savedMemberId = createAndSaveMember("사용자One");
        savedGroupId = createAndSaveGroup(savedMemberId, "테스트그룹");
        saveMemberGroup(savedMemberId, savedGroupId);
    }

    private List<AppointmentOption> createTestOptions() {
        Appointment appointment = createAndSaveAppointment(savedMemberId, "팀미팅");
        return List.of(
                AppointmentOption.of(
                        appointment,
                        LocalDateTime.of(2024, 3, 15, 0, 0),
                        LocalDateTime.of(2024, 3, 15, 10, 0),
                        LocalDateTime.of(2024, 3, 15, 10, 30)
                )
        );
    }

    private Map<Long, List<AppointmentMemberAvailableTime>> createTestMemberAvailability() {
        AppointmentMember member = createAppointmentMember(savedMemberId);

        return Map.of(
                savedMemberId, List.of(
                        AppointmentMemberAvailableTime.of(
                                member,
                                LocalDateTime.of(2024, 3, 15, 0, 0),
                                LocalDateTime.of(2024, 3, 15, 10, 0),
                                LocalDateTime.of(2024, 3, 15, 10, 30)
                        )
                )
        );
    }

    private Appointment createAndSaveAppointment(Long memberId, String appointmentName) {
        return appointmentRepository.save(
                Appointment.of(
                        groupRepository.findById(savedGroupId).orElseThrow(),
                        memberId,
                        appointmentName,
                        60L,
                        "중요",
                        org.noostak.appointment.domain.vo.AppointmentStatus.PROGRESS
                )
        );
    }

    private AppointmentMember createAppointmentMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("멤버가 존재하지 않습니다."));
        return AppointmentMember.of(AppointmentAvailability.AVAILABLE, createAndSaveAppointment(memberId, "팀미팅"), member);
    }


    private Long createAndSaveMember(String name) {
        Member member = memberRepository.save(Member.of(
                MemberName.from(name),
                MemberProfileImageKey.from("profile-key")
        ));
        return member.getId();
    }

    private Long createAndSaveGroup(Long memberId, String groupName) {
        Group group = groupRepository.save(Group.of(
                memberId,
                org.noostak.group.domain.vo.GroupName.from(groupName),
                org.noostak.group.domain.vo.GroupProfileImageKey.from("group-image-key"),
                "INVITE"
        ));
        return group.getId();
    }

    private void saveMemberGroup(Long memberId, Long groupId) {
        Member member = memberRepository.findById(memberId).orElseThrow();
        Group group = groupRepository.findById(groupId).orElseThrow();
        memberGroupRepository.save(MemberGroup.of(member, group));
    }

    private AppointmentMember createAppointmentMember(AppointmentAvailability availability, Long memberId, Long appointmentId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AppointmentException(AppointmentErrorCode.MEMBER_NOT_FOUND));
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentException(AppointmentErrorCode.APPOINTMENT_NOT_FOUND));

        return appointmentMemberRepository.save(AppointmentMember.of(availability, appointment, member));
    }
}
