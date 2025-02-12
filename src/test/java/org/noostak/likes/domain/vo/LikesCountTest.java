package org.noostak.likes.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.noostak.likes.common.LikesErrorCode;
import org.noostak.likes.common.LikesException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LikesCountTest {

    @Nested
    @DisplayName("성공 케이스")
    class SuccessCases {

        @Test
        @DisplayName("좋아요 수가 0인 객체 초기화")
        void shouldCreateWithZeroInitialValue() {
            // Given
            Long initialCount = 0L;

            // When
            LikesCount likesCount = LikesCount.from(initialCount);

            // Then
            assertThat(likesCount.value()).isEqualTo(initialCount);
        }

        @Test
        @DisplayName("좋아요 수 증가")
        void shouldIncreaseLikesCount() {
            // Given
            LikesCount likesCount = LikesCount.from(10L);

            // When
            LikesCount increasedLikesCount = likesCount.increase();

            // Then
            assertThat(increasedLikesCount.value()).isEqualTo(11L);
        }

        @Test
        @DisplayName("좋아요 수 감소")
        void shouldDecreaseLikesCount() {
            // Given
            LikesCount likesCount = LikesCount.from(10L);

            // When
            LikesCount decreasedLikesCount = likesCount.decrease();

            // Then
            assertThat(decreasedLikesCount.value()).isEqualTo(9L);
        }

        @Test
        @DisplayName("감소로 인한 좋아요 수는 최소 0을 유지")
        void shouldMinValueDecreasedLikesCount() {
            // Given
            LikesCount likesCount = LikesCount.from(0L);

            // When
            LikesCount decreasedLikesCount = likesCount.decrease();

            // Then
            assertThat(decreasedLikesCount.value()).isEqualTo(0L);
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class FailureCases {

        @Test
        @DisplayName("좋아요 수가 최대값을 초과할 경우 예외 발생")
        void shouldThrowExceptionWhenCountExceedsMaxLimit() {
            // Given
            LikesCount likesCount = LikesCount.from(50L);

            // When & Then
            assertThatThrownBy(likesCount::increase)
                    .isInstanceOf(LikesException.class)
                    .hasMessageContaining(LikesErrorCode.OVER_MAX_LIKES.getMessage());
        }

        @Test
        @DisplayName("초기값이 음수일 경우 예외 발생")
        void shouldThrowExceptionWhenInitialValueIsNegative() {
            // Given
            Long initialCount = -1L;

            // When & Then
            assertThatThrownBy(() -> LikesCount.from(initialCount))
                    .isInstanceOf(LikesException.class)
                    .hasMessageContaining(LikesErrorCode.LIKES_NOT_NEGATIVE.getMessage());
        }
    }
}
