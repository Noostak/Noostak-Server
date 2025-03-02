package org.noostak.group.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.noostak.group.common.exception.GroupErrorCode;
import org.noostak.group.common.exception.GroupException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GroupsInvitationCodeTest {

    @Nested
    @DisplayName("성공 케이스")
    class SuccessCases {

        @ParameterizedTest
        @DisplayName("유효한 초대 코드로 객체 생성")
        @CsvSource({
                "ABC123",
                "A1B2C3",
                "123456"
        })
        void shouldCreateCodeSuccessfully(String validCode) {
            // Given
            String code = validCode;

            // When
            GroupInvitationCode generatedCode = GroupInvitationCode.from(code);

            // Then
            assertThat(generatedCode.value()).isEqualTo(validCode);
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class FailureCases {

        @ParameterizedTest
        @DisplayName("초대 코드가 null이거나 비어 있는 경우")
        @NullAndEmptySource
        void shouldThrowExceptionForNullOrEmptyCode(String invalidCode) {
            // Given
            String code = invalidCode;

            // When & Then
            assertThatThrownBy(() -> GroupInvitationCode.from(code))
                    .isInstanceOf(GroupException.class)
                    .hasMessageContaining(GroupErrorCode.INVITE_CODE_NOT_EMPTY.getMessage());
        }

        @ParameterizedTest
        @DisplayName("초대 코드의 길이가 6이 아닌 경우")
        @CsvSource({
                "ABCDE",
                "ABCDEFG",
                "1234",
                "1234567"
        })
        void shouldThrowExceptionForInvalidCodeLength(String invalidCode) {
            // Given
            String code = invalidCode;

            // When & Then
            assertThatThrownBy(() -> GroupInvitationCode.from(code))
                    .isInstanceOf(GroupException.class)
                    .hasMessageContaining(GroupErrorCode.INVALID_INVITE_CODE_LENGTH.getMessage());
        }

        @ParameterizedTest
        @DisplayName("초대 코드에 알파벳이나 숫자가 아닌 문자가 포함된 경우")
        @CsvSource({
                "ABC@12",
                "123!@#",
                "AB12$%",
                "A1 2C3"
        })
        void shouldThrowExceptionForInvalidCharactersInCode(String invalidCode) {
            // Given
            String code = invalidCode;

            // When & Then
            assertThatThrownBy(() -> GroupInvitationCode.from(code))
                    .isInstanceOf(GroupException.class)
                    .hasMessageContaining(GroupErrorCode.INVALID_INVITE_CODE_ALPHA_NUMERIC_ONLY.getMessage());
        }

        @ParameterizedTest
        @DisplayName("초대 코드에 소문자가 포함된 경우")
        @CsvSource({
                "abc123",
                "AbC123",
                "a1b2c3"
        })
        void shouldThrowExceptionForLowerCaseCharactersInCode(String invalidCode) {
            // Given
            String code = invalidCode;

            // When & Then
            assertThatThrownBy(() -> GroupInvitationCode.from(code))
                    .isInstanceOf(GroupException.class)
                    .hasMessageContaining(GroupErrorCode.INVALID_INVITE_CODE_ALPHA_NUMERIC_ONLY.getMessage());
        }
    }
}