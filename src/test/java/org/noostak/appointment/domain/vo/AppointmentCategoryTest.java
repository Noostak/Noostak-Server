package org.noostak.appointment.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.noostak.appointment.common.exception.AppointmentErrorCode;
import org.noostak.appointment.common.exception.AppointmentException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AppointmentCategoryTest {

    @ParameterizedTest
    @CsvSource({
            "IMPORTANT, IMPORTANT",
            "important, IMPORTANT",
            "중요, IMPORTANT",
            "SCHEDULE, SCHEDULE",
            "schedule, SCHEDULE",
            "일정, SCHEDULE",
            "HOBBY, HOBBY",
            "hobby, HOBBY",
            "취미, HOBBY",
            "OTHER, OTHER",
            "other, OTHER",
            "기타, OTHER"
    })
    @DisplayName("유효한 카테고리 입력에 대해 올바른 AppointmentCategory 반환")
    void shouldReturnCorrectCategoryForValidInput(String input, AppointmentCategory expectedCategory) {
        // When
        AppointmentCategory category = AppointmentCategory.from(input);

        // Then
        assertThat(category).isEqualTo(expectedCategory);
    }

    @ParameterizedTest
    @CsvSource({"INVALID", "unknown", "wrong", "123"})
    @DisplayName("잘못된 카테고리 입력에 대해 예외 발생")
    void shouldThrowExceptionForInvalidCategory(String invalidCategory) {
        // When & Then
        assertThatThrownBy(() -> AppointmentCategory.from(invalidCategory))
                .isInstanceOf(AppointmentException.class)
                .hasMessageContaining(AppointmentErrorCode.APPOINTMENT_CATEGORY_NOT_FOUND.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("null 또는 빈 문자열 입력에 대해 예외 발생")
    void shouldThrowExceptionForNullOrBlankCategory(String nullOrBlankCategory) {
        // When & Then
        assertThatThrownBy(() -> AppointmentCategory.from(nullOrBlankCategory))
                .isInstanceOf(AppointmentException.class)
                .hasMessageContaining(AppointmentErrorCode.APPOINTMENT_CATEGORY_NULL_OR_BLANK.getMessage());
    }
}