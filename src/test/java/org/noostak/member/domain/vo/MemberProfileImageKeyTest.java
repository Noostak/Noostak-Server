package org.noostak.member.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.noostak.member.common.exception.MemberErrorCode;
import org.noostak.member.common.exception.MemberException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberProfileImageKeyTest {

    @Nested
    @DisplayName("성공 케이스")
    class SuccessCases {

        @ParameterizedTest
        @DisplayName("유효한 프로필 이미지 키로 객체 생성")
        @CsvSource({
                "profile-images/user123.jpg",
                "avatars/user456.png",
                "uploads/member789.jpeg",
                "user-folder/image-key123",
                "path/to/image.jpg",
                "folder/another-folder/my-image.png"
        })
        void shouldCreateMemberProfileImageKeySuccessfully(String validKey) {
            // Given & When
            MemberProfileImageKey profileImageKey = MemberProfileImageKey.from(validKey);

            // Then
            assertThat(profileImageKey.value()).isEqualTo(validKey);
        }

        @ParameterizedTest
        @DisplayName("null 값을 허용하는 경우")
        @NullSource
        void shouldAllowNullAsProfileImageKey(String nullableKey) {
            // Given & When
            MemberProfileImageKey profileImageKey = MemberProfileImageKey.from(nullableKey);

            // Then
            assertThat(profileImageKey.value()).isNull();
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class FailureCases {

        @ParameterizedTest
        @DisplayName("프로필 이미지 키가 비어있는 경우")
        @EmptySource
        void shouldThrowExceptionForEmptyProfileImageKey(String invalidKey) {
            // Given & When & Then
            assertThatThrownBy(() -> MemberProfileImageKey.from(invalidKey))
                    .isInstanceOf(MemberException.class)
                    .hasMessageContaining(MemberErrorCode.MEMBER_PROFILE_IMAGE_KEY_NOT_EMPTY.getMessage());
        }

        @ParameterizedTest
        @DisplayName("프로필 이미지 키가 공백만 포함하는 경우")
        @CsvSource({
                "' '",
                "'    '",
                "'\t'",
                "'\n'"
        })
        void shouldThrowExceptionForBlankProfileImageKey(String blankKey) {
            // Given & When & Then
            assertThatThrownBy(() -> MemberProfileImageKey.from(blankKey))
                    .isInstanceOf(MemberException.class)
                    .hasMessageContaining(MemberErrorCode.MEMBER_PROFILE_IMAGE_KEY_NOT_EMPTY.getMessage());
        }
    }
}
