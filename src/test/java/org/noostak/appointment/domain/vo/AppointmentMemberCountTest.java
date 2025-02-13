package org.noostak.appointment.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.noostak.appointment.common.exception.AppointmentErrorCode;
import org.noostak.appointment.common.exception.AppointmentException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AppointmentMemberCountTest {

    @Nested
    @DisplayName("성공 케이스")
    class SuccessCases {

        @Test
        @DisplayName("초기 멤버 수가 0인 객체 생성")
        void shouldCreateWithZeroInitialValue() {
            // Given
            Long initialCount = 0L;

            // When
            AppointmentMemberCount count = AppointmentMemberCount.from(initialCount);

            // Then
            assertThat(count.value()).isEqualTo(initialCount);
        }

        @Test
        @DisplayName("멤버 수 증가")
        void shouldIncreaseMemberCount() {
            // Given
            AppointmentMemberCount count = AppointmentMemberCount.from(10L);

            // When
            AppointmentMemberCount increasedCount = count.increase();

            // Then
            assertThat(increasedCount.value()).isEqualTo(11L);
        }

        @Test
        @DisplayName("멤버 수 감소")
        void shouldDecreaseMemberCount() {
            // Given
            AppointmentMemberCount count = AppointmentMemberCount.from(10L);

            // When
            AppointmentMemberCount decreasedCount = count.decrease();

            // Then
            assertThat(decreasedCount.value()).isEqualTo(9L);
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class FailureCases {

        @Test
        @DisplayName("멤버 수가 음수가 될 경우 예외 발생")
        void shouldThrowExceptionWhenCountIsNegative() {
            // Given
            AppointmentMemberCount count = AppointmentMemberCount.from(0L);

            // When & Then
            assertThatThrownBy(count::decrease)
                    .isInstanceOf(AppointmentException.class)
                    .hasMessageContaining(AppointmentErrorCode.APPOINTMENT_MEMBER_COUNT_NEGATIVE.getMessage());
        }

        @Test
        @DisplayName("멤버 수가 최대값을 초과할 경우 예외 발생")
        void shouldThrowExceptionWhenCountExceedsMaxLimit() {
            // Given
            AppointmentMemberCount count = AppointmentMemberCount.from(50L);

            // When & Then
            assertThatThrownBy(count::increase)
                    .isInstanceOf(AppointmentException.class)
                    .hasMessageContaining(AppointmentErrorCode.APPOINTMENT_MEMBER_COUNT_MAX.getMessage());
        }

        @Test
        @DisplayName("초기값이 음수일 경우 예외 발생")
        void shouldThrowExceptionWhenInitialValueIsNegative() {
            // Given
            Long initialCount = -1L;

            // When & Then
            assertThatThrownBy(() -> AppointmentMemberCount.from(initialCount))
                    .isInstanceOf(AppointmentException.class)
                    .hasMessageContaining(AppointmentErrorCode.APPOINTMENT_MEMBER_COUNT_NEGATIVE.getMessage());
        }
    }
}
