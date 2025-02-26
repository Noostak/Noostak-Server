package org.noostak.group.application.ongoing;

import org.junit.jupiter.api.*;
import org.noostak.appointment.application.recommendation.AppointmentRecommendationFacade;
import org.noostak.appointment.domain.*;
import org.noostak.appointment.domain.vo.AppointmentStatus;
import org.noostak.appointment.dto.response.*;
import org.noostak.appointmentmember.domain.*;
import org.noostak.appointmentmember.domain.repository.*;
import org.noostak.appointmentmember.domain.vo.AppointmentAvailability;
import org.noostak.group.domain.*;
import org.noostak.group.domain.vo.GroupName;
import org.noostak.group.domain.vo.GroupProfileImageKey;
import org.noostak.group.dto.response.ongoing.*;
import org.noostak.infra.S3Service;
import org.noostak.member.MemberRepositoryTest;
import org.noostak.member.domain.*;
import org.noostak.member.domain.vo.*;
import org.noostak.membergroup.MemberGroupRepositoryTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class GroupOngoingAppointmentServiceImplTest {

    private AppointmentRecommendationFacade recommendationFacade;
    private final S3Service s3Service = mock(S3Service.class);
    private GroupOngoingAppointmentServiceImpl service;

    private AppointmentRepositoryTest appointmentRepository;
    private GroupRepositoryTest groupRepository;
    private MemberRepositoryTest memberRepository;
    private MemberGroupRepositoryTest memberGroupRepository;
    private AppointmentMemberRepositoryTest appointmentMemberRepository;
    private AppointmentMemberAvailableTimesRepositoryTest availableTimesRepository;
    private AppointmentHostSelectionTimeRepositoryTest hostSelectionTimeRepository;

    private Long savedMemberId;
    private Long savedGroupId;

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
        @DisplayName("그룹 내 모든 약속에서 가용 인원이 가장 많은 옵션을 반환한다.")
        void shouldReturnBestAvailableOptionForEachAppointment() {
            // Given
            Appointment appointment1 = createAndSaveAppointment("약속1");
            Appointment appointment2 = createAndSaveAppointment("약속2");

            saveAppointmentMember(AppointmentAvailability.AVAILABLE, savedMemberId, appointment1.getId());
            saveAppointmentMember(AppointmentAvailability.AVAILABLE, savedMemberId, appointment2.getId());

            mockRecommendedOptions(appointment1, 5L);
            mockRecommendedOptions(appointment2, 8L);

            // When
            GroupOngoingAppointmentsResponse response = service.getGroupOngoingAppointments(savedMemberId, savedGroupId);

            // Then
            assertThat(response).isNotNull();
            assertThat(response.ongoingAppointments()).hasSize(2);
            assertThat(response.ongoingAppointments().get(0).availableGroupMemberCount()).isEqualTo(5L);
            assertThat(response.ongoingAppointments().get(1).availableGroupMemberCount()).isEqualTo(8L);
        }
    }

    @Nested
    @DisplayName("실패 케이스 검증")
    class FailureCases {

        @Test
        @DisplayName("추천 옵션이 존재하지 않으면 빈 응답을 반환한다.")
        void shouldReturnEmptyResponseWhenNoRecommendedOptionsExist() {
            // Given
            Appointment appointment = createAndSaveAppointment("추천 없음");
            when(recommendationFacade.getRecommendedOptions(savedMemberId, appointment.getId(), appointment))
                    .thenReturn(List.of());

            // When
            GroupOngoingAppointmentsResponse response = service.getGroupOngoingAppointments(savedMemberId, savedGroupId);

            // Then
            assertThat(response).isNotNull();
            assertThat(response.ongoingAppointments()).isEmpty();
        }
    }

    private void initializeRepositories() {
        appointmentRepository = new AppointmentRepositoryTest();
        groupRepository = new GroupRepositoryTest();
        memberRepository = new MemberRepositoryTest();
        memberGroupRepository = new MemberGroupRepositoryTest();
        appointmentMemberRepository = new AppointmentMemberRepositoryTest();
        availableTimesRepository = new AppointmentMemberAvailableTimesRepositoryTest();
        hostSelectionTimeRepository = new AppointmentHostSelectionTimeRepositoryTest();

        recommendationFacade = mock(AppointmentRecommendationFacade.class);
        service = new GroupOngoingAppointmentServiceImpl(recommendationFacade, groupRepository, appointmentRepository, s3Service);
    }

    private void initializeTestData() {
        savedMemberId = createAndSaveMember("사용자One");
        savedGroupId = createAndSaveGroup(savedMemberId, "테스트그룹");
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
                GroupName.from(groupName),
                GroupProfileImageKey.from("group-image-key"),
                "INVITE"
        ));
        return group.getId();
    }

    private Appointment createAndSaveAppointment(String name) {
        return appointmentRepository.save(
                Appointment.of(
                        groupRepository.findById(savedGroupId).orElseThrow(),
                        savedMemberId,
                        name,
                        60L,
                        "중요",
                        AppointmentStatus.PROGRESS
                )
        );
    }

    private void saveAppointmentMember(AppointmentAvailability availability, Long memberId, Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow();
        Member member = memberRepository.findById(memberId).orElseThrow();

        AppointmentMember appointmentMember = AppointmentMember.of(availability, appointment, member);
        appointmentMemberRepository.save(appointmentMember);

        availableTimesRepository.saveAll(List.of(
                AppointmentMemberAvailableTime.of(
                        appointmentMember,
                        LocalDateTime.of(2024, 3, 15, 10, 0),
                        LocalDateTime.of(2024, 3, 15, 10, 0),
                        LocalDateTime.of(2024, 3, 15, 10, 30)
                )
        ));
    }

    private void mockRecommendedOptions(Appointment appointment, Long availableMemberCount) {
        AppointmentOptionResponse optionResponse = AppointmentOptionResponse.of(
                1L,
                10L,
                false,
                20L,
                availableMemberCount,
                AppointmentOptionTimeResponse.of(
                        LocalDateTime.of(2024, 3, 15, 10, 0),
                        LocalDateTime.of(2024, 3, 15, 10, 30),
                        LocalDateTime.of(2024, 3, 15, 11, 0)
                ),
                AppointmentMyInfoResponse.of("AVAILABLE", 1L, "사용자1"),
                AvailableFriendsResponse.of(3L, List.of("친구1", "친구2", "친구3")),
                UnavailableFriendsResponse.of(2L, List.of("친구4", "친구5"))
        );

        AppointmentPriorityGroupResponse priorityGroup = AppointmentPriorityGroupResponse.of(
                1L,
                List.of(optionResponse)
        );

        List<AppointmentPriorityGroupResponse> recommendedOptions = List.of(priorityGroup);

        when(recommendationFacade.getRecommendedOptions(savedMemberId, appointment.getId(), appointment))
                .thenReturn(recommendedOptions);
    }
}
