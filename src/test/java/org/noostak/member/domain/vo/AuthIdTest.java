package org.noostak.member.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.noostak.auth.domain.vo.AuthId;
import org.noostak.member.common.exception.MemberErrorCode;
import org.noostak.member.common.exception.MemberException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AuthIdTest {

    @Nested
    @DisplayName("성공 케이스")
    class SuccessCases {

        @ParameterizedTest
        @DisplayName("유효한 Auth ID로 객체 생성")
        @CsvSource({
                "user123",
                "auth-id-456",
                "abcdefg",
                "validUserId",
                "ID_98765",
                "unique.auth.id",
                "simpleid"
        })
        void shouldCreateCodeSuccessfully(String validId) {
            // Given & When
            AuthId authId = AuthId.from(validId);

            // Then
            assertThat(authId.value()).isEqualTo(validId);
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class FailureCases {

        @ParameterizedTest
        @DisplayName("Auth ID가 null인 경우 예외 발생")
        @NullSource
        void shouldThrowExceptionForNullAuthId(String nullId) {
            // Given & When & Then
            assertThatThrownBy(() -> AuthId.from(nullId))
                    .isInstanceOf(MemberException.class)
                    .hasMessageContaining(MemberErrorCode.AUTH_ID_NOT_NULL.getMessage());
        }

        @ParameterizedTest
        @DisplayName("Auth ID가 빈 문자열인 경우 예외 발생")
        @EmptySource
        void shouldThrowExceptionForEmptyAuthId(String emptyId) {
            // Given & When & Then
            assertThatThrownBy(() -> AuthId.from(emptyId))
                    .isInstanceOf(MemberException.class)
                    .hasMessageContaining(MemberErrorCode.AUTH_ID_NOT_EMPTY.getMessage());
        }

        @ParameterizedTest
        @DisplayName("Auth ID가 공백만 포함하는 경우 예외 발생")
        @CsvSource({
                "' '",
                "'    '",
                "'\t'",
                "'\n'"
        })
        void shouldThrowExceptionForBlankAuthId(String blankId) {
            // Given & When & Then
            assertThatThrownBy(() -> AuthId.from(blankId))
                    .isInstanceOf(MemberException.class)
                    .hasMessageContaining(MemberErrorCode.AUTH_ID_NOT_EMPTY.getMessage());
        }
    }
}
