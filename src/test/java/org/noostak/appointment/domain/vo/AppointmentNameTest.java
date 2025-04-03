package org.noostak.appointment.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
                "프로젝트 회의",
                "미팅@123",
                "테스트#이름",
                "개발모임*스터디",
                "새로운~회의",
                "중요 회의!",
        })
        void shouldCreateAppointmentNameSuccessfully(String validName) {
            AppointmentName appointmentName = AppointmentName.from(validName);
            assertThat(appointmentName.value()).isEqualTo(validName);
        }

        @ParameterizedTest
        @DisplayName("약속 이름이 20자 이하일 경우 성공")
        @ValueSource(strings = {
                "12345678901234567890", // 20자
                "약속 이름입니다."       // 한글 포함된 예시
        })
        void shouldAllowNameWithUpTo20Characters(String validName) {
            AppointmentName appointmentName = AppointmentName.from(validName);
            assertThat(appointmentName.value()).isEqualTo(validName);
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class FailureCases {

        @ParameterizedTest
        @DisplayName("약속 이름이 20자를 초과하는 경우")
        @ValueSource(strings = {
                "123456789012345678901", // 21자
        })
        void shouldThrowExceptionForNameExceedingMaxLength(String invalidName) {
            assertThatThrownBy(() -> AppointmentName.from(invalidName))
                    .isInstanceOf(AppointmentException.class)
                    .hasMessageContaining(AppointmentErrorCode.INVALID_APPOINTMENT_NAME_LENGTH.getMessage());
        }
    }
}
