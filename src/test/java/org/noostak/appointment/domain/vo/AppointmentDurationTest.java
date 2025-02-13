package org.noostak.appointment.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.noostak.appointment.common.exception.AppointmentErrorCode;
import org.noostak.appointment.common.exception.AppointmentException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AppointmentDurationTest {

    @Nested
    @DisplayName("성공 케이스")
    class SuccessCases {

        @ParameterizedTest
        @CsvSource({"0", "60", "120", "240", "1440"})
        @DisplayName("유효한 약속 시간을 가진 객체 생성")
        void shouldCreateWithValidDuration(Long validDuration) {
            // When
            AppointmentDuration duration = AppointmentDuration.from(validDuration);

            // Then
            assertThat(duration.value()).isEqualTo(validDuration);
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class FailureCases {

        @ParameterizedTest
        @CsvSource({"-10", "-1"})
        @DisplayName("약속 시간이 음수일 경우 예외 발생")
        void shouldThrowExceptionWhenDurationIsNegative(Long invalidDuration) {
            // When & Then
            assertThatThrownBy(() -> AppointmentDuration.from(invalidDuration))
                    .isInstanceOf(AppointmentException.class)
                    .hasMessageContaining(AppointmentErrorCode.APPOINTMENT_DURATION_NEGATIVE.getMessage());
        }

        @ParameterizedTest
        @CsvSource({"1441", "1500", "2000"})
        @DisplayName("약속 시간이 1440분을 초과할 경우 예외 발생")
        void shouldThrowExceptionWhenDurationExceedsMaxLimit(Long invalidDuration) {
            // When & Then
            assertThatThrownBy(() -> AppointmentDuration.from(invalidDuration))
                    .isInstanceOf(AppointmentException.class)
                    .hasMessageContaining(AppointmentErrorCode.APPOINTMENT_DURATION_MAX.getMessage());
        }

        @ParameterizedTest
        @CsvSource({"1", "59", "125", "130", "500"})
        @DisplayName("약속 시간이 60분 단위가 아닐 경우 예외 발생")
        void shouldThrowExceptionWhenDurationIsNotMultipleOfSixty(Long invalidDuration) {
            // When & Then
            assertThatThrownBy(() -> AppointmentDuration.from(invalidDuration))
                    .isInstanceOf(AppointmentException.class)
                    .hasMessageContaining(AppointmentErrorCode.APPOINTMENT_DURATION_INVALID_UNIT.getMessage());
        }
    }
}