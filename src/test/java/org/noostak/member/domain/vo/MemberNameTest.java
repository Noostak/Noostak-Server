package org.noostak.member.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.noostak.member.common.MemberErrorCode;
import org.noostak.member.common.MemberException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class MemberNameTest {

    @Nested
    @DisplayName("성공 케이스")
    class SuccessCases {

        @ParameterizedTest
        @DisplayName("유효한 이름으로 객체 생성")
        @CsvSource({
                "Alice",
                "한글이름",
                "JohnDoe",
                "영한혼합",
                "홍길동",
                "홍길동😊",
                "😀홍길동",
                "Alice😀홍길동"
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
    class FailureCases{

            @ParameterizedTest
            @DisplayName("이름이 null이거나 비어 있는 경우")
            @NullAndEmptySource
            void shouldThrowExceptionForNullOrEmptyName(String invalidName) {
                // Given & When & Then
                assertThatThrownBy(() -> MemberName.from(invalidName))
                        .isInstanceOf(MemberException.class)
                        .hasMessageContaining(MemberErrorCode.MEMBER_NAME_NOT_EMPTY.getMessage());
            }

            @ParameterizedTest
            @DisplayName("이름의 길이가 15자를 초과하는 경우")
            @CsvSource({
                    "abcdefghijklmnop",
                    "이모지와문자가혼합된이름입니다😊😊😊😊"
            })
            void shouldThrowExceptionForNameLengthExceeded(String invalidName) {
                // Given & When & Then
                assertThatThrownBy(() -> MemberName.from(invalidName))
                        .isInstanceOf(MemberException.class)
                        .hasMessageContaining(MemberErrorCode.MEMBER_NAME_LENGTH_EXCEEDED.getMessage());
            }

            @ParameterizedTest
            @DisplayName("이름에 특수문자가 포함된 경우")
            @CsvSource({
                    "Alice!",
                    "Alice@",
                    "Alice#",
                    "Alice$",
                    "Alice%",
                    "Alice^",
                    "Alice&",
                    "Alice*",
                    "Alice(",
                    "Alice)",
                    "Alice_",
                    "Alice+",
                    "Alice=",
                    "Alice|",
                    "Alice<",
                    "Alice>",
                    "Alice?",
                    "Alice{",
                    "Alice}",
                    "Alice[",
                    "Alice]",
                    "Alice~",
                    "Alice-"
            })
            void shouldThrowExceptionForInvalidCharacter(String invalidName) {
                // Given & When & Then
                assertThatThrownBy(() -> MemberName.from(invalidName))
                        .isInstanceOf(MemberException.class)
                        .hasMessageContaining(MemberErrorCode.MEMBER_NAME_INVALID_CHARACTER.getMessage());
            }
    }
}