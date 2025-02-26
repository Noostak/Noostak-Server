package org.noostak.appointment.application.recommendation;

import org.junit.jupiter.api.*;
import org.noostak.appointment.application.recommendation.impl.AppointmentMemberAvailabilityQueryServiceImpl;
import org.noostak.appointment.common.exception.AppointmentErrorCode;
import org.noostak.appointment.common.exception.AppointmentException;
import org.noostak.appointment.domain.*;
import org.noostak.appointmentmember.domain.*;
import org.noostak.appointmentmember.domain.repository.AppointmentMemberAvailableTimesRepositoryTest;
import org.noostak.appointmentmember.domain.repository.AppointmentMemberRepositoryTest;
import org.noostak.appointmentmember.domain.vo.AppointmentAvailability;
import org.noostak.group.domain.Group;
import org.noostak.group.domain.GroupRepositoryTest;
import org.noostak.member.MemberRepositoryTest;
import org.noostak.member.domain.*;
import org.noostak.member.domain.vo.*;
import org.noostak.membergroup.domain.MemberGroup;
import org.noostak.membergroup.MemberGroupRepositoryTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class AppointmentMemberAvailabilityQueryServiceTest {

    private AppointmentMemberAvailabilityQueryService queryService;
    private AppointmentMemberAvailableTimesRepositoryTest availableTimesRepository;
    private AppointmentMemberRepositoryTest appointmentMemberRepository;
    private AppointmentRepositoryTest appointmentRepository;
    private GroupRepositoryTest groupRepository;
    private MemberRepositoryTest memberRepository;
    private MemberGroupRepositoryTest memberGroupRepository;

    private Long savedMemberId;
    private Long savedGroupId;
    private Long savedAppointmentId;

    @BeforeEach
    void setUp() {
        initializeRepositories();
        initializeTestData();
    }

    @AfterEach
    void tearDown() {
        availableTimesRepository.deleteAll();
        appointmentMemberRepository.deleteAll();
        appointmentRepository.deleteAll();
        memberRepository.deleteAll();
        groupRepository.deleteAll();
        memberGroupRepository.deleteAll();
    }

    @Nested
    @DisplayName("성공 케이스 검증")
    class SuccessCases {

        @Test
        @DisplayName("1️⃣ 멤버의 가용 시간을 정상적으로 조회한다.")
        void shouldFindAvailableTimeSlotsSuccessfully() {
            // When
            Map<Long, List<AppointmentMemberAvailableTime>> availableTimeSlots =
                    queryService.findAvailableTimeSlotsByAppointmentId(savedAppointmentId);

            // Then
            assertThat(availableTimeSlots)
                    .withFailMessage("멤버 가용 시간이 비어 있음!")
                    .isNotEmpty();
            assertThat(availableTimeSlots).containsKey(savedMemberId);
            assertThat(availableTimeSlots.get(savedMemberId)).hasSize(2);
        }
    }

    @Nested
    @DisplayName("실패 케이스 검증")
    class FailureCases {

        @Test
        @DisplayName("저장된 멤버 가용 시간이 없을 경우 예외 발생")
        void shouldFailWhenNoMemberAvailabilityFound() {
            // Given
            availableTimesRepository.deleteAll();

            // When & Then
            assertThatThrownBy(() -> queryService.findAvailableTimeSlotsByAppointmentId(savedAppointmentId))
                    .isInstanceOf(AppointmentException.class)
                    .hasMessage(AppointmentErrorCode.MEMBER_AVAILABILITY_NOT_FOUND.getMessage());
        }
    }

    private void initializeRepositories() {
        availableTimesRepository = new AppointmentMemberAvailableTimesRepositoryTest();
        appointmentMemberRepository = new AppointmentMemberRepositoryTest();
        appointmentRepository = new AppointmentRepositoryTest();
        groupRepository = new GroupRepositoryTest();
        memberRepository = new MemberRepositoryTest();
        memberGroupRepository = new MemberGroupRepositoryTest();
        queryService = new AppointmentMemberAvailabilityQueryServiceImpl(availableTimesRepository);
    }

    private void initializeTestData() {
        savedMemberId = createAndSaveMember("사용자One");
        savedGroupId = createAndSaveGroup(savedMemberId, "테스트그룹");
        saveMemberGroup(savedMemberId, savedGroupId);
        savedAppointmentId = createAndSaveAppointment(savedMemberId, "팀미팅");
        saveMemberAvailability(savedMemberId, savedAppointmentId);
    }

    private Long createAndSaveMember(String name) {
        Member member = memberRepository.save(Member.of(
                MemberName.from(name),
                MemberProfileImageKey.from("profile-key"),
                AuthType.GOOGLE,
                AuthId.from("auth-id"),
                "refresh-token"
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

    private Long createAndSaveAppointment(Long memberId, String appointmentName) {
        Appointment appointment = appointmentRepository.save(
                Appointment.of(
                        groupRepository.findById(savedGroupId).orElseThrow(),
                        memberId,
                        appointmentName,
                        60L,
                        "중요",
                        org.noostak.appointment.domain.vo.AppointmentStatus.PROGRESS
                )
        );
        return appointment.getId();
    }

    private void saveMemberAvailability(Long memberId, Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow();
        Member member = memberRepository.findById(memberId).orElseThrow();
        AppointmentMember appointmentMember = createAppointmentMember(AppointmentAvailability.AVAILABLE, memberId, appointmentId);

        LocalDateTime date = LocalDateTime.of(2024, 3, 15, 0, 0);

        List<AppointmentMemberAvailableTime> availableTimes = List.of(
                AppointmentMemberAvailableTime.of(appointmentMember, date, LocalDateTime.of(2024, 3, 15, 10, 0), LocalDateTime.of(2024, 3, 15, 10, 30)),
                AppointmentMemberAvailableTime.of(appointmentMember, date, LocalDateTime.of(2024, 3, 15, 10, 30), LocalDateTime.of(2024, 3, 15, 11, 0))
        );

        availableTimesRepository.saveAll(availableTimes);
        appointmentMember.updateAvailableTimes(availableTimes);
    }

    private AppointmentMember createAppointmentMember(AppointmentAvailability availability, Long memberId, Long appointmentId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AppointmentException(AppointmentErrorCode.MEMBER_NOT_FOUND));
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentException(AppointmentErrorCode.APPOINTMENT_NOT_FOUND));

        return appointmentMemberRepository.save(AppointmentMember.of(availability, appointment, member));
    }
}
