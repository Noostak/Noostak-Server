package org.noostak.appointment.application.recommendation;

import org.junit.jupiter.api.*;
import org.noostak.appointment.application.recommendation.impl.AppointmentOptionCommandServiceImpl;
import org.noostak.appointment.domain.*;
import org.noostak.appointment.dto.response.AppointmentOptionAvailabilityResponse;
import org.noostak.appointmentoption.domain.*;
import org.noostak.group.domain.Group;
import org.noostak.group.domain.GroupRepositoryTest;
import org.noostak.member.MemberRepositoryTest;
import org.noostak.member.domain.*;
import org.noostak.member.domain.vo.*;
import org.noostak.membergroup.MemberGroupRepositoryTest;
import org.noostak.membergroup.domain.MemberGroup;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class AppointmentOptionCommandServiceTest {

    private AppointmentOptionCommandService commandService;
    private AppointmentOptionRepositoryTest appointmentOptionRepository;
    private AppointmentRepositoryTest appointmentRepository;
    private GroupRepositoryTest groupRepository;
    private MemberRepositoryTest memberRepository;
    private MemberGroupRepositoryTest memberGroupRepository;

    private Long savedAppointmentId;
    private Appointment savedAppointment;

    @BeforeEach
    void setUp() {
        initializeRepositories();
        initializeTestData();
    }

    @AfterEach
    void tearDown() {
        appointmentOptionRepository.deleteAll();
        appointmentRepository.deleteAll();
        memberRepository.deleteAll();
        groupRepository.deleteAll();
        memberGroupRepository.deleteAll();
    }

    @Nested
    @DisplayName("성공 케이스 검증")
    class SuccessCases {

        @Test
        @DisplayName("약속 옵션이 정상적으로 저장된다.")
        void shouldSaveAppointmentOptionsSuccessfully() {
            List<AppointmentOptionAvailabilityResponse> optionDTOs = createOptionDTOs();

            List<AppointmentOption> savedOptions = commandService.saveOptions(savedAppointment, optionDTOs);

            assertThat(savedOptions).isNotEmpty();
            assertThat(savedOptions).hasSize(2);
            assertThat(savedOptions.get(0).getDate()).isEqualTo(getTestDateTime());
        }
    }

    @Nested
    @DisplayName("실패 케이스 검증")
    class FailureCases {

        @Test
        @DisplayName("빈 옵션 리스트가 주어질 경우 빈 리스트 반환")
        void shouldReturnEmptyListWhenOptionsAreEmpty() {
            List<AppointmentOptionAvailabilityResponse> emptyOptions = List.of();

            List<AppointmentOption> savedOptions = commandService.saveOptions(savedAppointment, emptyOptions);

            assertThat(savedOptions).isEmpty();
        }
    }

    private void initializeRepositories() {
        appointmentOptionRepository = new AppointmentOptionRepositoryTest();
        appointmentRepository = new AppointmentRepositoryTest();
        groupRepository = new GroupRepositoryTest();
        memberRepository = new MemberRepositoryTest();
        memberGroupRepository = new MemberGroupRepositoryTest();
        commandService = new AppointmentOptionCommandServiceImpl(appointmentOptionRepository);
    }

    private void initializeTestData() {
        Long savedMemberId = createAndSaveMember("사용자One");
        Long savedGroupId = createAndSaveGroup(savedMemberId, "테스트그룹");
        saveMemberGroup(savedMemberId, savedGroupId);
        savedAppointmentId = createAndSaveAppointment(savedMemberId, "팀미팅");
        savedAppointment = appointmentRepository.findById(savedAppointmentId).orElseThrow();
    }

    private List<AppointmentOptionAvailabilityResponse> createOptionDTOs() {
        return List.of(
                new AppointmentOptionAvailabilityResponse(
                        getTestDateTime(),
                        LocalDateTime.of(2024, 3, 15, 10, 0),
                        LocalDateTime.of(2024, 3, 15, 10, 30),
                        List.of(1L, 2L)
                ),
                new AppointmentOptionAvailabilityResponse(
                        getTestDateTime(),
                        LocalDateTime.of(2024, 3, 15, 10, 30),
                        LocalDateTime.of(2024, 3, 15, 11, 0),
                        List.of(2L, 3L)
                )
        );
    }

    private LocalDateTime getTestDateTime() {
        return LocalDateTime.of(2024, 3, 15, 0, 0);
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
                        groupRepository.findById(memberId).orElseThrow(),
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
