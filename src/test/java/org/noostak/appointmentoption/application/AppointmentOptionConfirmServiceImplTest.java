package org.noostak.appointmentoption.application;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.noostak.appointment.domain.Appointment;
import org.noostak.appointment.domain.AppointmentRepositoryTest;
import org.noostak.appointment.domain.vo.AppointmentStatus;
import org.noostak.appointmentoption.common.exception.AppointmentOptionErrorCode;
import org.noostak.appointmentoption.common.exception.AppointmentOptionException;
import org.noostak.appointmentoption.domain.AppointmentOption;
import org.noostak.appointmentoption.domain.AppointmentOptionRepositoryTest;
import org.noostak.appointmentoption.domain.vo.AppointmentOptionStatus;
import org.noostak.group.domain.Group;
import org.noostak.group.domain.GroupRepositoryTest;
import org.noostak.group.domain.vo.GroupName;
import org.noostak.group.domain.vo.GroupProfileImageKey;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@Transactional
class AppointmentOptionConfirmServiceImplTest {

    private AppointmentOptionConfirmServiceImpl appointmentOptionConfirmService;
    private AppointmentOptionRepositoryTest appointmentOptionRepository;
    private AppointmentRepositoryTest appointmentRepository;
    private GroupRepositoryTest groupRepository;

    private static final LocalDateTime FIXED_DATE = LocalDateTime.of(2024, 3, 15, 0, 0);
    private static final LocalDateTime FIXED_START_TIME = LocalDateTime.of(2024, 3, 15, 10, 0);
    private static final LocalDateTime FIXED_END_TIME = LocalDateTime.of(2024, 3, 15, 10, 30);

    @BeforeEach
    void setUp() {
        groupRepository = new GroupRepositoryTest();
        appointmentRepository = new AppointmentRepositoryTest();
        appointmentOptionRepository = new AppointmentOptionRepositoryTest();
        appointmentOptionConfirmService = new AppointmentOptionConfirmServiceImpl(appointmentOptionRepository);
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
        @DisplayName("약속 옵션을 정상적으로 확정할 수 있다.")
        void shouldConfirmAppointmentOptionSuccessfully() {
            // Given
            Group group = createAndSaveGroup();
            Appointment appointment = createAndSaveAppointment(group);
            AppointmentOption savedOption = createAndSaveAppointmentOption(appointment);

            // When
            assertDoesNotThrow(() -> appointmentOptionConfirmService.confirmAppointment(savedOption.getId()));

            // Then
            AppointmentOption confirmedOption = appointmentOptionRepository.findById(savedOption.getId()).orElseThrow();
            assertThat(confirmedOption.getStatus()).isEqualTo(AppointmentOptionStatus.CONFIRMED);
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class FailureCases {

        @Test
        @DisplayName("존재하지 않는 약속 옵션을 확정하려고 하면 예외가 발생한다.")
        void shouldThrowExceptionWhenOptionNotFound() {
            // Given
            Long nonExistentOptionId = 999L;

            // When & Then
            assertThatThrownBy(() -> appointmentOptionConfirmService.confirmAppointment(nonExistentOptionId))
                    .isInstanceOf(AppointmentOptionException.class)
                    .hasMessage(AppointmentOptionErrorCode.APPOINTMENT_OPTION_NOT_FOUND.getMessage());
        }
    }

    private Group createAndSaveGroup() {
        return groupRepository.save(
                Group.of(1L, GroupName.from("테스트그룹"), GroupProfileImageKey.from("group-image-key"), "INVITE")
        );
    }

    private Appointment createAndSaveAppointment(Group group) {
        return appointmentRepository.save(
                Appointment.of(group, 1L, "더미약속", 60L, "중요", AppointmentStatus.PROGRESS)
        );
    }

    private AppointmentOption createAndSaveAppointmentOption(Appointment appointment) {
        return appointmentOptionRepository.save(
                AppointmentOption.of(appointment, FIXED_DATE, FIXED_START_TIME, FIXED_END_TIME)
        );
    }
}
