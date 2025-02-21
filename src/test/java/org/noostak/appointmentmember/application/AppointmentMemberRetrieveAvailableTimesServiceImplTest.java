package org.noostak.appointmentmember.application;

import org.junit.jupiter.api.*;
import org.noostak.appointment.domain.Appointment;
import org.noostak.appointment.domain.AppointmentHostSelectionTimeRepositoryTest;
import org.noostak.appointment.domain.AppointmentRepositoryTest;
import org.noostak.appointmentmember.common.exception.AppointmentMemberErrorCode;
import org.noostak.appointmentmember.common.exception.AppointmentMemberException;
import org.noostak.appointmentmember.domain.AppointmentMember;
import org.noostak.appointmentmember.domain.AppointmentMemberAvailableTime;
import org.noostak.appointmentmember.domain.repository.AppointmentMemberAvailableTimesRepositoryTest;
import org.noostak.appointmentmember.domain.repository.AppointmentMemberRepositoryTest;
import org.noostak.appointmentmember.domain.vo.AppointmentAvailability;
import org.noostak.appointmentmember.dto.response.AppointmentMemberAvailableTimeResponse;
import org.noostak.appointmentmember.dto.response.AppointmentMemberInfoResponse;
import org.noostak.appointmentmember.dto.response.AppointmentMembersAvailableTimesResponse;
import org.noostak.group.domain.Group;
import org.noostak.group.domain.GroupRepositoryTest;
import org.noostak.member.MemberRepositoryTest;
import org.noostak.member.domain.Member;
import org.noostak.member.domain.vo.AuthId;
import org.noostak.member.domain.vo.AuthType;
import org.noostak.member.domain.vo.MemberName;
import org.noostak.member.domain.vo.MemberProfileImageKey;
import org.noostak.membergroup.MemberGroupRepositoryTest;
import org.noostak.membergroup.domain.MemberGroup;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
public class AppointmentMemberRetrieveAvailableTimesServiceImplTest {

    private AppointmentMemberRetrieveAvailableTimesService appointmentMemberRetrieveAvailableTimesService;
    private AppointmentMemberRepositoryTest appointmentMemberRepository;
    private AppointmentMemberAvailableTimesRepositoryTest appointmentMemberAvailableTimesRepository;
    private AppointmentRepositoryTest appointmentRepository;
    private MemberRepositoryTest memberRepository;
    private MemberGroupRepositoryTest memberGroupRepository;
    private GroupRepositoryTest groupRepository;
    private AppointmentHostSelectionTimeRepositoryTest appointmentHostSelectionTimeRepository;

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
        appointmentMemberAvailableTimesRepository.deleteAll();
        appointmentMemberRepository.deleteAll();
        appointmentRepository.deleteAll();
        memberRepository.deleteAll();
        groupRepository.deleteAll();
        memberGroupRepository.deleteAll();
    }

    @Nested
    @DisplayName("성공 케이스")
    class SuccessCases {

        @Test
        @DisplayName("멤버의 약속 가능 시간을 정상적으로 조회할 수 있다.")
        void shouldRetrieveAvailableTimesSuccessfully() {
            AppointmentMembersAvailableTimesResponse response =
                    appointmentMemberRetrieveAvailableTimesService.retrieveAvailableTimes(savedMemberId, savedAppointmentId);

            assertThat(response).isNotNull();
            assertThat(response.isAppointMemberTimeSet()).isTrue();
        }

        @Test
        @DisplayName("특정 시간대의 약속 가능 시간이 올바르게 조회된다.")
        void shouldRetrieveCorrectAvailableTimes() {
            // Given
            AppointmentMembersAvailableTimesResponse response =
                    appointmentMemberRetrieveAvailableTimesService.retrieveAvailableTimes(savedMemberId, savedAppointmentId);

            // When
            List<AppointmentMemberAvailableTimeResponse> availableTimes = response.appointmentScheduleResponse()
                    .appointmentMemberInfoResponse().get(0)
                    .appointmentMemberAvailableTimesResponse().appointmentMemberAvailableTimesResponseResponse();

            // Then
            assertThat(availableTimes).hasSize(2);
            assertThat(availableTimes.get(0).startTime()).isEqualTo(LocalDateTime.of(2024, 3, 15, 10, 0));
            assertThat(availableTimes.get(0).endTime()).isEqualTo(LocalDateTime.of(2024, 3, 15, 10, 30));
            assertThat(availableTimes.get(1).startTime()).isEqualTo(LocalDateTime.of(2024, 3, 15, 11, 0));
            assertThat(availableTimes.get(1).endTime()).isEqualTo(LocalDateTime.of(2024, 3, 15, 11, 30));
        }

        @Test
        @DisplayName("여러 명의 멤버가 동일한 약속을 가질 때 각각의 가능 시간이 올바르게 조회된다.")
        void shouldRetrieveAvailableTimesForMultipleMembers() {
            // Given
            Long secondMemberId = createAndSaveMember("사용자Two");
            saveAppointmentMember(AppointmentAvailability.AVAILABLE, secondMemberId, savedAppointmentId);

            // When
            AppointmentMembersAvailableTimesResponse response =
                    appointmentMemberRetrieveAvailableTimesService.retrieveAvailableTimes(savedMemberId, savedAppointmentId);

            List<AppointmentMemberInfoResponse> membersInfo = response.appointmentScheduleResponse().appointmentMemberInfoResponse();

            // Then
            assertThat(response).isNotNull();
            assertThat(membersInfo).hasSize(2);

            // 두 멤버가 각각의 가능 시간을 가지고 있는지 확인
            List<Long> memberIds = membersInfo.stream().map(AppointmentMemberInfoResponse::memberId).toList();
            assertThat(memberIds).containsExactlyInAnyOrder(savedMemberId, secondMemberId);

            // 각 멤버가 2개의 가능 시간을 갖고 있는지 확인
            for (AppointmentMemberInfoResponse memberInfo : membersInfo) {
                assertThat(memberInfo.appointmentMemberAvailableTimesResponse().appointmentMemberAvailableTimesResponseResponse())
                        .hasSize(2);
            }
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class FailureCases {

        @Test
        @DisplayName("약속 멤버가 아닌 사용자가 조회하면 예외가 발생한다.")
        void shouldThrowExceptionWhenUserIsNotAppointmentMember() {
            Long invalidMemberId = 999L;

            assertThatThrownBy(() ->
                    appointmentMemberRetrieveAvailableTimesService.retrieveAvailableTimes(invalidMemberId, savedAppointmentId))
                    .isInstanceOf(AppointmentMemberException.class)
                    .hasMessage(AppointmentMemberErrorCode.APPOINTMENT_MEMBER_NOT_FOUND.getMessage());
        }

        @Test
        @DisplayName("존재하지 않는 멤버가 조회하면 예외가 발생한다.")
        void shouldThrowExceptionWhenMemberDoesNotExist() {
            Long nonExistentMemberId = 888L;

            assertThatThrownBy(() ->
                    appointmentMemberRetrieveAvailableTimesService.retrieveAvailableTimes(nonExistentMemberId, savedAppointmentId))
                    .isInstanceOf(AppointmentMemberException.class)
                    .hasMessage(AppointmentMemberErrorCode.APPOINTMENT_MEMBER_NOT_FOUND.getMessage());
        }

        @Test
        @DisplayName("존재하지 않는 약속을 조회하면 예외가 발생한다.")
        void shouldThrowExceptionWhenAppointmentDoesNotExist() {
            Long nonExistentAppointmentId = 777L;

            assertThatThrownBy(() ->
                    appointmentMemberRetrieveAvailableTimesService.retrieveAvailableTimes(savedMemberId, nonExistentAppointmentId))
                    .isInstanceOf(AppointmentMemberException.class)
                    .hasMessage(AppointmentMemberErrorCode.APPOINTMENT_MEMBER_NOT_FOUND.getMessage());
        }

        @Test
        @DisplayName("멤버는 존재하지만 약속과 연결되지 않은 경우 예외가 발생한다.")
        void shouldThrowExceptionWhenMemberIsNotLinkedToAppointment() {
            Long anotherMemberId = createAndSaveMember("사용자Three");

            assertThatThrownBy(() ->
                    appointmentMemberRetrieveAvailableTimesService.retrieveAvailableTimes(anotherMemberId, savedAppointmentId))
                    .isInstanceOf(AppointmentMemberException.class)
                    .hasMessage(AppointmentMemberErrorCode.APPOINTMENT_MEMBER_NOT_FOUND.getMessage());
        }
    }

    private void initializeRepositories() {
        appointmentMemberRepository = new AppointmentMemberRepositoryTest();
        appointmentMemberAvailableTimesRepository = new AppointmentMemberAvailableTimesRepositoryTest();
        appointmentRepository = new AppointmentRepositoryTest();
        groupRepository = new GroupRepositoryTest();
        memberRepository = new MemberRepositoryTest();
        memberGroupRepository = new MemberGroupRepositoryTest();
        appointmentHostSelectionTimeRepository = new AppointmentHostSelectionTimeRepositoryTest();
        appointmentMemberRetrieveAvailableTimesService = new AppointmentMemberRetrieveAvailableTimesServiceImpl(
                appointmentHostSelectionTimeRepository, appointmentMemberAvailableTimesRepository, appointmentMemberRepository
        );
    }

    private void initializeTestData() {
        initializeMemberAndGroup();
        initializeAppointmentData();
    }

    private void initializeMemberAndGroup() {
        savedMemberId = createAndSaveMember("사용자One");
        savedGroupId = createAndSaveGroup(savedMemberId, "테스트그룹");
        saveMemberGroup(savedMemberId, savedGroupId);
    }

    private void initializeAppointmentData() {
        savedAppointmentId = createAndSaveAppointment(savedMemberId, "팀미팅");
        saveAppointmentMember(AppointmentAvailability.AVAILABLE, savedMemberId, savedAppointmentId);
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

    private void saveAppointmentMember(AppointmentAvailability availability, Long memberId, Long appointmentId) {
        AppointmentMember appointmentMember = createAppointmentMember(availability, memberId, appointmentId);
        List<AppointmentMemberAvailableTime> availableTimes = createAvailableTimes(appointmentMember);

        appointmentMemberAvailableTimesRepository.saveAll(availableTimes);

        appointmentMember.updateAvailableTimes(availableTimes);
    }

    private AppointmentMember createAppointmentMember(AppointmentAvailability availability, Long memberId, Long appointmentId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AppointmentMemberException(AppointmentMemberErrorCode.APPOINTMENT_MEMBER_NOT_FOUND));
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentMemberException(AppointmentMemberErrorCode.APPOINTMENT_NOT_FOUND));

        return appointmentMemberRepository.save(AppointmentMember.of(availability, appointment, member));
    }

    private List<AppointmentMemberAvailableTime> createAvailableTimes(AppointmentMember appointmentMember) {
        return List.of(
                AppointmentMemberAvailableTime.of(appointmentMember, LocalDateTime.of(2024, 3, 15, 10, 0), LocalDateTime.of(2024, 3, 15, 10, 0), LocalDateTime.of(2024, 3, 15, 10, 30)),
                AppointmentMemberAvailableTime.of(appointmentMember, LocalDateTime.of(2024, 3, 15, 11, 0), LocalDateTime.of(2024, 3, 15, 11, 0), LocalDateTime.of(2024, 3, 15, 11, 30))
        );
    }
}
