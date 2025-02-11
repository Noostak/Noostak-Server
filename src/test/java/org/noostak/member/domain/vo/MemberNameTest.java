package org.noostak.member.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.noostak.member.common.MemberErrorCode;
import org.noostak.member.common.MemberException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("멤버 이름 테스트")
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
                "홍길동"
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
            // Given & When & Then
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
            // Given & When & Then
            assertThatThrownBy(() -> MemberName.from(invalidName))
                    .isInstanceOf(MemberException.class)
                    .hasMessageContaining(MemberErrorCode.MEMBER_NAME_LENGTH_EXCEEDED.getMessage());
        }

        @ParameterizedTest
        @DisplayName("이름에 특수문자가 포함된 경우")
        @CsvSource({
                "jsoon!",
                "jsoon@",
                "jsoon#",
                "jsoon$",
                "jsoon%",
                "jsoon^",
                "jsoon&",
                "jsoon*",
                "jsoon(",
                "jsoon)",
                "jsoon_",
                "jsoon+",
                "jsoon=",
                "jsoon|",
                "jsoon<",
                "jsoon>",
                "jsoon?",
                "jsoon{",
                "jsoon}",
                "jsoon[",
                "jsoon]",
                "jsoon~",
                "jsoon-"
        })
        void shouldThrowExceptionForInvalidSpecialCharacters(String invalidName) {
            // Given & When & Then
            assertThatThrownBy(() -> MemberName.from(invalidName))
                    .isInstanceOf(MemberException.class)
                    .hasMessageContaining(MemberErrorCode.MEMBER_NAME_CANNOT_CONTAIN_SPECIAL_CHARACTERS.getMessage());
        }

        @ParameterizedTest
        @DisplayName("이름에 허용되지 않은 언어(한글/영어 이외의 문자)가 포함된 경우")
        @CsvSource({
                "张伟",
                "山田太郎",
                "علي",
                "Иван",
                "jsoon张",
                "こんにちは홍길동",
                "Русский홍길동"
        })
        void shouldThrowExceptionForInvalidLanguages(String invalidName) {
            // Given & When & Then
            assertThatThrownBy(() -> MemberName.from(invalidName))
                    .isInstanceOf(MemberException.class)
                    .hasMessageContaining(MemberErrorCode.MEMBER_NAME_INVALID_LANGUAGE.getMessage());
        }

        @ParameterizedTest
        @DisplayName("이름에 숫자가 포함된 경우")
        @CsvSource({
                "홍길동1",
                "jsoon123",
                "Test007",
                "12jsoon"
        })
        void shouldThrowExceptionForNameContainingNumbers(String invalidName) {
            // Given & When & Then
            assertThatThrownBy(() -> MemberName.from(invalidName))
                    .isInstanceOf(MemberException.class)
                    .hasMessageContaining(MemberErrorCode.MEMBER_NAME_CANNOT_CONTAIN_NUMBERS.getMessage());
        }

        @ParameterizedTest
        @DisplayName("이름에 특수문자가 포함된 경우")
        @CsvSource({
                "홍길동!",
                "jsoon@",
                "Test#jsoon",
                "jsoon&",
                "hello*",
                "이름%",
                "이름~이름",
                "홍길동{",
                "홍길동}",
                "이름[",
                "이름]",
                "이름+",
                "이름=",
                "이름_",
                "이름|",
                "이름/",
                "이름'",
                "이름;",
                "이름:",
                "이름<",
                "이름>",
                "이름?",
                "이름^"
        })
        void shouldThrowExceptionForNameContainingSpecialCharacters(String invalidName) {
            assertThatThrownBy(() -> MemberName.from(invalidName))
                    .isInstanceOf(MemberException.class)
                    .hasMessageContaining(MemberErrorCode.MEMBER_NAME_CANNOT_CONTAIN_SPECIAL_CHARACTERS.getMessage());
        }


        @ParameterizedTest
        @DisplayName("이름에 허용되지 않은 언어가 포함된 경우")
        @CsvSource({
                "张伟",
                "山田太郎",
                "علي",
                "Иван",
                "jsoon张",
                "こんにちは홍길동",
                "Русский홍길동"
        })
        void shouldThrowExceptionForNameContainingInvalidLanguage(String invalidName) {
            // Given & When & Then
            assertThatThrownBy(() -> MemberName.from(invalidName))
                    .isInstanceOf(MemberException.class)
                    .hasMessageContaining(MemberErrorCode.MEMBER_NAME_INVALID_LANGUAGE.getMessage());
        }
    }
}
