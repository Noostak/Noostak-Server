package org.noostak.appointment.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.noostak.appointment.common.exception.AppointmentErrorCode;
import org.noostak.appointment.common.exception.AppointmentException;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AppointmentCategoryTest {

    @Nested
    @DisplayName("성공 케이스")
    class SuccessCases {

        @ParameterizedTest
        @CsvSource({
                "중요, IMPORTANT",
                "일정, SCHEDULE",
                "취미, HOBBY",
                "기타, OTHER"
        })
        @DisplayName("올바른 한글 카테고리 입력 시 AppointmentCategory 반환")
        void shouldReturnCorrectCategoryForValidKoreanInput(String input, AppointmentCategory expectedCategory) {
            // When
            AppointmentCategory category = AppointmentCategory.from(input);

            // Then
            assertThat(category).isEqualTo(expectedCategory);
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class FailureCases {

        @ParameterizedTest
        @CsvSource({
                "INVALID", "unknown", "wrong", "123",
                "important", "IMPORTANT",
                "schedule", "SCHEDULE",
                "hobby", "HOBBY",
                "other", "OTHER"
        })
        @DisplayName("잘못된 입력(한글 카테고리 외 단어) 시 예외 발생")
        void shouldThrowExceptionForInvalidCategory(String invalidCategory) {
            // When & Then
            assertThatThrownBy(() -> AppointmentCategory.from(invalidCategory))
                    .isInstanceOf(AppointmentException.class)
                    .hasMessage(AppointmentErrorCode.APPOINTMENT_CATEGORY_NOT_FOUND.getMessage());
        }

        @ParameterizedTest
        @MethodSource("provideNullOrBlankCategories")
        @DisplayName("null 또는 빈 문자열(공백 포함) 입력 시 예외 발생")
        void shouldThrowExceptionForNullOrBlankCategory(String nullOrBlankCategory) {
            // When & Then
            assertThatThrownBy(() -> AppointmentCategory.from(nullOrBlankCategory))
                    .isInstanceOf(AppointmentException.class)
                    .hasMessage(AppointmentErrorCode.APPOINTMENT_CATEGORY_NULL_OR_BLANK.getMessage());
        }

        private static Stream<Arguments> provideNullOrBlankCategories() {
            return Stream.of(
                    Arguments.of((String) null),
                    Arguments.of(""),
                    Arguments.of("   ")
            );
        }
    }
}
