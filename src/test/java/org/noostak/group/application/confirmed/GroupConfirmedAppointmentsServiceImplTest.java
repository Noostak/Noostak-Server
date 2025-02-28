package org.noostak.group.application.confirmed;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.*;
import org.noostak.appointment.domain.Appointment;
import org.noostak.appointment.domain.AppointmentRepositoryTest;
import org.noostak.appointment.domain.vo.AppointmentStatus;
import org.noostak.appointmentoption.domain.AppointmentOption;
import org.noostak.appointmentoption.domain.AppointmentOptionRepositoryTest;
import org.noostak.group.common.exception.GroupErrorCode;
import org.noostak.group.common.exception.GroupException;
import org.noostak.group.domain.Group;
import org.noostak.group.domain.GroupRepositoryTest;
import org.noostak.group.domain.vo.GroupInvitationCode;
import org.noostak.group.domain.vo.GroupName;
import org.noostak.group.domain.vo.GroupProfileImageKey;
import org.noostak.group.dto.response.confirmed.GroupConfirmedAppointmentsResponse;
import org.noostak.infra.S3Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

@Transactional
class GroupConfirmedAppointmentsServiceImplTest {

    private GroupConfirmedAppointmentsServiceImpl service;
    private GroupRepositoryTest groupRepository;
    private AppointmentRepositoryTest appointmentRepository;
    private AppointmentOptionRepositoryTest appointmentOptionRepository;
    private final S3Service s3Service = mock(S3Service.class);

    private Long savedGroupId;
    private static final LocalDateTime FIXED_DATE = LocalDateTime.of(2024, 3, 15, 0, 0);
    private static final LocalDateTime FIXED_START_TIME = LocalDateTime.of(2024, 3, 15, 10, 0);
    private static final LocalDateTime FIXED_END_TIME = LocalDateTime.of(2024, 3, 15, 12, 0);

    @BeforeEach
    void setUp() {
        groupRepository = new GroupRepositoryTest();
        appointmentRepository = new AppointmentRepositoryTest();
        appointmentOptionRepository = new AppointmentOptionRepositoryTest();

        service = new GroupConfirmedAppointmentsServiceImpl(groupRepository, appointmentRepository, appointmentOptionRepository, s3Service);

        savedGroupId = createAndSaveGroup();
    }

    @AfterEach
    void tearDown() {
        appointmentOptionRepository.deleteAll();
        appointmentRepository.deleteAll();
        groupRepository.deleteAll();
    }

    @Nested
    @DisplayName("성공 케이스")
    class SuccessCases {

        @Test
        @DisplayName("확정된 약속 목록을 정상적으로 조회할 수 있다.")
        void shouldGetConfirmedAppointmentsSuccessfully() {
            // Given
            Appointment appointment1 = createAndSaveAppointment("회의", "중요");
            Appointment appointment2 = createAndSaveAppointment("친목모임", "취미");

            AppointmentOption option1 = createAndSaveConfirmedAppointmentOption(appointment1);
            AppointmentOption option2 = createAndSaveConfirmedAppointmentOption(appointment2);

            // When
            GroupConfirmedAppointmentsResponse response = service.getGroupConfirmedAppointments(1L, savedGroupId);

            // Then
            assertThat(response).isNotNull();
            assertThat(response.confirmedAppointmentsResponse()).hasSize(2);

            assertThat(response.confirmedAppointmentsResponse())
                    .extracting("appointmentId", "appointmentName", "category")
                    .containsExactlyInAnyOrder(
                            Tuple.tuple(appointment1.getId(), "회의", appointment1.getCategory().getMessage()),
                            Tuple.tuple(appointment2.getId(), "친목모임", appointment2.getCategory().getMessage())
                    );
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class FailureCases {

        @Test
        @DisplayName("존재하지 않는 그룹에서 조회하면 예외 발생")
        void shouldThrowExceptionWhenGroupNotFound() {
            Long nonExistentGroupId = 999L;

            assertThatThrownBy(() -> service.getGroupConfirmedAppointments(1L, nonExistentGroupId))
                    .isInstanceOf(GroupException.class)
                    .hasMessage(GroupErrorCode.GROUP_NOT_FOUND.getMessage());
        }

        @Test
        @DisplayName("확정된 약속이 없는 경우 빈 목록을 반환해야 한다.")
        void shouldReturnEmptyListWhenNoConfirmedAppointments() {
            // When
            GroupConfirmedAppointmentsResponse response = service.getGroupConfirmedAppointments(1L, savedGroupId);

            // Then
            assertThat(response).isNotNull();
            assertThat(response.confirmedAppointmentsResponse()).isEmpty();
        }
    }

    private Long createAndSaveGroup() {
        Group group = Group.of(
                1L,
                GroupName.from("스터디그룹"),
                GroupProfileImageKey.from("group-image-key"),
                GroupInvitationCode.from("INVITE").value()
        );
        return groupRepository.save(group).getId();
    }

    private Appointment createAndSaveAppointment(String name, String category) {
        return appointmentRepository.save(
                Appointment.of(
                        getSavedGroup(),
                        1L,
                        name,
                        60L,
                        category,
                        AppointmentStatus.CONFIRMED
                )
        );
    }

    private AppointmentOption createAndSaveConfirmedAppointmentOption(Appointment appointment) {
        AppointmentOption option = AppointmentOption.of(
                appointment,
                FIXED_DATE,
                FIXED_START_TIME,
                FIXED_END_TIME
        );

        option.confirm();
        return appointmentOptionRepository.save(option);
    }

    private Group getSavedGroup() {
        return groupRepository.findById(savedGroupId)
                .orElseThrow(() -> new GroupException(GroupErrorCode.GROUP_NOT_FOUND));
    }
}
