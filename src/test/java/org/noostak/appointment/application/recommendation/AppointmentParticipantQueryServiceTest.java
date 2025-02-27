package org.noostak.appointment.application.recommendation;

import org.junit.jupiter.api.*;
import org.noostak.appointment.application.recommendation.impl.AppointmentParticipantQueryServiceImpl;
import org.noostak.appointment.domain.*;
import org.noostak.appointmentmember.common.exception.*;
import org.noostak.appointmentmember.domain.*;
import org.noostak.appointmentmember.domain.repository.*;
import org.noostak.appointmentmember.domain.vo.AppointmentAvailability;
import org.noostak.group.domain.*;
import org.noostak.member.MemberRepositoryTest;
import org.noostak.member.domain.*;
import org.noostak.member.domain.vo.*;
import org.noostak.membergroup.*;
import org.noostak.membergroup.domain.MemberGroup;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class AppointmentParticipantQueryServiceTest {

    private AppointmentMemberRepositoryTest appointmentMemberRepository;
    private AppointmentMemberAvailableTimesRepositoryTest appointmentMemberAvailableTimesRepository;
    private AppointmentRepositoryTest appointmentRepository;
    private MemberRepositoryTest memberRepository;
    private MemberGroupRepositoryTest memberGroupRepository;
    private GroupRepositoryTest groupRepository;
    private AppointmentHostSelectionTimeRepositoryTest appointmentHostSelectionTimeRepository;

    private AppointmentParticipantQueryService participantQueryService;

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
    @DisplayName("성공 케이스 검증")
    class SuccessCases {

        @Test
        @DisplayName("약속 ID로 참여자 이름을 정상적으로 조회한다.")
        void shouldFindParticipantNamesByAppointmentId() {
            // When
            Map<Long, String> participantNames = participantQueryService.findParticipantNamesByAppointmentId(savedAppointmentId);

            // Then
            assertThat(participantNames).isNotEmpty();
            assertThat(participantNames).containsKey(savedMemberId);
            assertThat(participantNames.get(savedMemberId)).isEqualTo("사용자One");
        }
    }

    @Nested
    @DisplayName("실패 케이스 검증")
    class FailureCases {

        @Test
        @DisplayName("해당 약속 ID의 참여자가 없을 경우 빈 맵 반환")
        void shouldReturnEmptyMapWhenNoParticipantsFound() {
            // Given
            Long invalidAppointmentId = 999L;

            // When
            Map<Long, String> participantNames = participantQueryService.findParticipantNamesByAppointmentId(invalidAppointmentId);

            // Then
            assertThat(participantNames).isEmpty();
        }
    }

    private void initializeRepositories() {
        appointmentMemberRepository = new AppointmentMemberRepositoryTest();
        appointmentMemberAvailableTimesRepository = new AppointmentMemberAvailableTimesRepositoryTest();
        appointmentRepository = new AppointmentRepositoryTest();
        memberRepository = new MemberRepositoryTest();
        groupRepository = new GroupRepositoryTest();
        memberGroupRepository = new MemberGroupRepositoryTest();
        appointmentHostSelectionTimeRepository = new AppointmentHostSelectionTimeRepositoryTest();

        participantQueryService = new AppointmentParticipantQueryServiceImpl(appointmentMemberAvailableTimesRepository);
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
                AppointmentMemberAvailableTime.of(appointmentMember,
                        LocalDateTime.of(2024, 3, 15, 10, 0),
                        LocalDateTime.of(2024, 3, 15, 10, 0),
                        LocalDateTime.of(2024, 3, 15, 10, 30)),

                AppointmentMemberAvailableTime.of(appointmentMember,
                        LocalDateTime.of(2024, 3, 15, 11, 0),
                        LocalDateTime.of(2024, 3, 15, 11, 0),
                        LocalDateTime.of(2024, 3, 15, 11, 30))
        );
    }
}
