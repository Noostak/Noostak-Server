package org.noostak.appointment.application.recommendation;

import org.junit.jupiter.api.*;
import org.noostak.appointment.common.exception.AppointmentErrorCode;
import org.noostak.appointment.common.exception.AppointmentException;
import org.noostak.appointment.domain.*;
import org.noostak.appointment.util.TimeSlot;
import org.noostak.group.domain.Group;
import org.noostak.group.domain.GroupRepository;
import org.noostak.group.domain.GroupRepositoryTest;
import org.noostak.member.MemberRepositoryTest;
import org.noostak.member.domain.Member;
import org.noostak.member.domain.MemberRepository;
import org.noostak.member.domain.vo.MemberName;
import org.noostak.member.domain.vo.MemberProfileImageKey;
import org.noostak.membergroup.MemberGroupRepositoryTest;
import org.noostak.membergroup.domain.MemberGroup;
import org.noostak.membergroup.domain.MemberGroupRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class AppointmentHostSelectedTimeQueryServiceImplTest {

    private AppointmentHostSelectedTimeQueryService queryService;
    private AppointmentHostSelectionTimeRepositoryTest appointmentHostSelectionTimeRepository;
    private AppointmentRepository appointmentRepository;
    private MemberRepository memberRepository;
    private GroupRepository groupRepository;
    private MemberGroupRepository memberGroupRepository;

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
        appointmentHostSelectionTimeRepository.deleteAll();
        appointmentRepository.deleteAll();
        memberRepository.deleteAll();
        groupRepository.deleteAll();
        memberGroupRepository.deleteAll();
    }

    @Nested
    @DisplayName("성공 케이스 검증")
    class SuccessCases {

        @Test
        @DisplayName("1️⃣ 호스트 선택 시간이 정상적으로 분할된다.")
        void shouldSplitHostSelectedTimeSlotsSuccessfully() {
            // Given
            Long duration = 30L;

            // When
            List<TimeSlot> timeSlots = queryService.splitHostSelectedTimeSlots(savedAppointmentId, duration);

            // Then
            assertThat(timeSlots)
                    .withFailMessage("분할된 TimeSlot 리스트가 비어 있음! appointmentId: " + savedAppointmentId)
                    .isNotEmpty();
            assertThat(timeSlots.get(0).start()).isEqualTo(LocalDateTime.of(2024, 3, 15, 10, 0));
            assertThat(timeSlots.get(timeSlots.size() - 1).end()).isEqualTo(LocalDateTime.of(2024, 3, 15, 11, 0));
        }
    }

    @Nested
    @DisplayName("실패 케이스 검증")
    class FailureCases {

        @Test
        @DisplayName("저장된 호스트 선택 시간이 없을 경우 예외 발생")
        void shouldFailWhenHostSelectionTimeNotFound() {
            // Given
            appointmentHostSelectionTimeRepository.deleteByAppointmentId(savedAppointmentId);

            // When & Then
            assertThatThrownBy(() -> queryService.splitHostSelectedTimeSlots(savedAppointmentId, 60L))
                    .isInstanceOf(AppointmentException.class)
                    .hasMessage(AppointmentErrorCode.HOST_SELECTION_TIME_NOT_FOUND.getMessage());
        }
    }

    private void saveHostSelectionTime(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentException(AppointmentErrorCode.APPOINTMENT_NOT_FOUND));

        LocalDateTime startTime = LocalDateTime.of(2024, 3, 15, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 3, 15, 11, 0);

        AppointmentHostSelectionTime selectionTime = AppointmentHostSelectionTime.of(
                appointment,
                startTime.toLocalDate().atStartOfDay(),
                startTime,
                endTime
        );
        appointmentHostSelectionTimeRepository.save(selectionTime);
    }

    private void initializeRepositories() {
        appointmentHostSelectionTimeRepository = new AppointmentHostSelectionTimeRepositoryTest();
        appointmentRepository = new AppointmentRepositoryTest();
        memberRepository = new MemberRepositoryTest();
        groupRepository = new GroupRepositoryTest();
        memberGroupRepository = new MemberGroupRepositoryTest();
        queryService = new org.noostak.appointment.application.recommendation.impl.AppointmentHostSelectedTimeQueryServiceImpl(appointmentHostSelectionTimeRepository);
    }


    private void initializeTestData() {
        savedMemberId = createAndSaveMember("사용자One");
        savedGroupId = createAndSaveGroup(savedMemberId, "테스트그룹");
        saveMemberGroup(savedMemberId, savedGroupId);
        savedAppointmentId = createAndSaveAppointment(savedMemberId, "팀미팅");
        saveHostSelectionTime(savedAppointmentId);
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
}
