package org.noostak.appointment.domain.vo;

import org.assertj.core.util.Strings;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.noostak.appointment.common.exception.AppointmentErrorCode;
import org.noostak.appointment.common.exception.AppointmentException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AppointmentNameTest {

    @Nested
    @DisplayName("성공 케이스")
    class SuccessCases {

        @ParameterizedTest
        @DisplayName("유효한 약속 이름으로 객체 생성")
        @CsvSource({
                "팀 미팅",
                "회의",
                "Lunch Meeting",
                "개발자 모임",
                "스터디 그룹",
                "프로젝트 회의"
        })
        void shouldCreateAppointmentNameSuccessfully(String validName) {
            // Given & When
            AppointmentName appointmentName = AppointmentName.from(validName);

            // Then
            assertThat(appointmentName.value()).isEqualTo(validName);
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class FailureCases {

        @ParameterizedTest
        @DisplayName("약속 이름이 null이거나 비어 있는 경우")
        @NullAndEmptySource
        void shouldThrowExceptionForNullOrEmptyName(String invalidName) {
            assertThatThrownBy(() -> AppointmentName.from(invalidName))
                    .isInstanceOf(AppointmentException.class)
                    .hasMessageContaining(AppointmentErrorCode.APPOINTMENT_NAME_NOT_EMPTY.getMessage());
        }

        @ParameterizedTest
        @DisplayName("약속 이름이 공백만 포함된 경우")
        @ValueSource(strings = {" ", "  ", "\t", "\n"})
        void shouldThrowExceptionForOnlyWhitespaceName(String invalidName) {
            assertThatThrownBy(() -> AppointmentName.from(invalidName))
                    .isInstanceOf(AppointmentException.class)
                    .hasMessageContaining(AppointmentErrorCode.APPOINTMENT_NAME_NOT_EMPTY.getMessage());
        }

        @ParameterizedTest
        @DisplayName("약속 이름의 길이가 50자를 초과하는 경우")
        @CsvSource({
                "abcdefghijklmnopqrstuvwxyz12345678901234567890aaaaaaaabbbbbbbb",
                "회의이름이너무길어서이것은유효하지않습니다이것은50자초과입니다정말로길다길어이거는너무길다rrrrrrrrrr"
        })
        void shouldThrowExceptionForNameExceedingMaxLength(String invalidName) {
            assertThatThrownBy(() -> AppointmentName.from(invalidName))
                    .isInstanceOf(AppointmentException.class)
                    .hasMessageContaining(AppointmentErrorCode.INVALID_APPOINTMENT_NAME_LENGTH.getMessage());
        }

        @ParameterizedTest
        @DisplayName("약속 이름에 허용되지 않은 특수 문자가 포함된 경우")
        @CsvSource({
                "미팅@123",
                "테스트#이름",
                "개발모임*스터디",
                "새로운~회의",
                "중요 회의!"
        })
        void shouldThrowExceptionForNameWithSpecialCharacters(String invalidName) {
            assertThatThrownBy(() -> AppointmentName.from(invalidName))
                    .isInstanceOf(AppointmentException.class)
                    .hasMessageContaining(AppointmentErrorCode.INVALID_APPOINTMENT_NAME_CHARACTER.getMessage());
        }
    }
}
