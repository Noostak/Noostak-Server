package org.noostak.member.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.noostak.member.common.exception.MemberErrorCode;
import org.noostak.member.common.exception.MemberException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberNameTest {

    @Nested
    @DisplayName("성공 케이스")
    class SuccessCases {

        @ParameterizedTest
        @DisplayName("유효한 이름으로 객체 생성")
        @CsvSource({
                "jsoonworld",
                "한글이름",
                "jsoon",
                "영한혼합",
                "홍길동",
        })
        void shouldCreateMemberNameSuccessfully(String validName) {
            // Given & When
            MemberName memberName = MemberName.from(validName);

            // Then
            assertThat(memberName.value()).isEqualTo(validName);
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class FailureCases {

        @ParameterizedTest
        @DisplayName("이름이 null이거나 비어 있는 경우")
        @NullAndEmptySource
        void shouldThrowExceptionForNullOrEmptyName(String invalidName) {
            assertThatThrownBy(() -> MemberName.from(invalidName))
                    .isInstanceOf(MemberException.class)
                    .hasMessageContaining(MemberErrorCode.MEMBER_NAME_NOT_EMPTY.getMessage());
        }

        @ParameterizedTest
        @DisplayName("이름의 길이가 15자를 초과하는 경우")
        @CsvSource({
                "abcdefghijklmnop",
                "한글과영문혼합길이초과abcde"
        })
        void shouldThrowExceptionForNameLengthExceeded(String invalidName) {
            assertThatThrownBy(() -> MemberName.from(invalidName))
                    .isInstanceOf(MemberException.class)
                    .hasMessageContaining(MemberErrorCode.MEMBER_NAME_LENGTH_EXCEEDED.getMessage());
        }

        @ParameterizedTest
        @DisplayName("이름에 허용되지 않은 언어가 포함된 경우")
        @CsvSource({
                "علي",
                "Иван",
                "こんにちは홍길동",
                "Русский홍길동"
        })
        void shouldThrowExceptionForNameContainingInvalidLanguage(String invalidName) {
            assertThatThrownBy(() -> MemberName.from(invalidName))
                    .isInstanceOf(MemberException.class)
                    .hasMessageContaining(MemberErrorCode.INVALID_MEMBER_NAME.getMessage());
        }
    }
}
