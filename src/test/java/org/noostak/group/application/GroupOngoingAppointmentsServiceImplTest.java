package org.noostak.group.application;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.noostak.appointment.domain.Appointment;
import org.noostak.appointment.domain.AppointmentRepositoryTest;
import org.noostak.appointment.domain.vo.AppointmentStatus;
import org.noostak.group.application.ongoing.GroupOngoingAppointmentsServiceImpl;
import org.noostak.group.common.exception.GroupErrorCode;
import org.noostak.group.common.exception.GroupException;
import org.noostak.group.domain.Group;
import org.noostak.group.domain.GroupRepositoryTest;
import org.noostak.group.domain.vo.GroupName;
import org.noostak.group.domain.vo.GroupProfileImageKey;
import org.noostak.group.dto.response.ongoing.GroupOngoingAppointmentsResponse;
import org.noostak.infra.S3Service;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class GroupOngoingAppointmentsServiceImplTest {

    private GroupOngoingAppointmentsServiceImpl groupOngoingAppointmentsService;
    private GroupRepositoryTest groupRepository;
    private AppointmentRepositoryTest appointmentRepository;
    private S3Service s3Service;

    @BeforeEach
    void setUp() {
        initializeRepositories();
        initializeTestData();
    }

    @AfterEach
    void tearDown() {
        appointmentRepository.deleteAll();
        groupRepository.deleteAll();
    }

    @Nested
    @DisplayName("성공 케이스")
    class SuccessCases {

        @Test
        @DisplayName("진행 중인 약속을 정상적으로 조회할 수 있다.")
        void shouldGetOngoingAppointmentsSuccessfully() {
            // Given
            Group group = createAndSaveGroup();
            createAndSaveAppointment(group, "회의1", AppointmentStatus.PROGRESS);
            createAndSaveAppointment(group, "회의2", AppointmentStatus.PROGRESS);
            createAndSaveAppointment(group, "종료된회의", AppointmentStatus.CONFIRMED);

            // When
            GroupOngoingAppointmentsResponse response = groupOngoingAppointmentsService.getGroupOngoingAppointments(group.getId());

            // Then
            assertThat(response).isNotNull();
            assertThat(response.groupOngoingInfo()).isNotNull();
            assertThat(response.ongoingAppointments()).hasSize(2);
            assertThat(response.ongoingAppointments()).extracting("appointmentName")
                    .containsExactlyInAnyOrder("회의1", "회의2");
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class FailureCases {

        @Test
        @DisplayName("존재하지 않는 그룹의 진행 중인 약속을 조회하면 예외가 발생한다.")
        void shouldThrowExceptionWhenGroupNotFound() {
            // Given
            Long nonExistentGroupId = 999L;

            // When & Then
            assertThatThrownBy(() -> groupOngoingAppointmentsService.getGroupOngoingAppointments(nonExistentGroupId))
                    .isInstanceOf(GroupException.class)
                    .hasMessage(GroupErrorCode.GROUP_NOT_FOUND.getMessage());
        }
    }

    private void initializeRepositories() {
        groupRepository = new GroupRepositoryTest();
        appointmentRepository = new AppointmentRepositoryTest();
        s3Service = Mockito.mock(S3Service.class);

        groupOngoingAppointmentsService = new GroupOngoingAppointmentsServiceImpl(
                groupRepository, appointmentRepository, s3Service
        );

        Mockito.when(s3Service.getImageUrl(Mockito.any())).thenReturn("https://fake-url.com/group-image.jpg");
    }

    private void initializeTestData() {
        createAndSaveGroup();
    }

    private Group createAndSaveGroup() {
        return groupRepository.save(
                Group.of(1L, GroupName.from("테스트그룹"), GroupProfileImageKey.from("group-image-key"), "INVITE")
        );
    }

    private Appointment createAndSaveAppointment(Group group, String name, AppointmentStatus status) {
        return appointmentRepository.save(
                Appointment.of(group, 1L, name, 60L, "중요", status)
        );
    }
}
