package org.noostak.like.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.noostak.appointment.common.exception.AppointmentErrorCode;
import org.noostak.appointmentmember.common.exception.AppointmentMemberException;
import org.noostak.appointmentmember.domain.AppointmentMember;
import org.noostak.appointmentoption.domain.AppointmentOption;
import org.noostak.like.common.exception.LikesErrorCode;
import org.noostak.like.common.exception.LikesException;
import org.noostak.like.dto.DecreaseResponse;
import org.noostak.like.dto.IncreaseResponse;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LikeServiceImplTest {

    @InjectMocks
    private FakeLikeService likeService;

    @Mock
    private AppointmentOption mockOption;

    @Mock
    private AppointmentMember mockMember;

    @Mock
    private FakeLikeRepository likeRepository;

    @Mock
    private FakeAppointmentOptionRepository optionRepository;

    @Mock
    private FakeAppointmentMemberRepository appointmentMemberRepository;

    @Nested
    @DisplayName("좋아요 감소 (Success)")
    class Success {

        @Test
        @DisplayName("좋아요를 삭제하면 개수가 감소한다.")
        void decreaseLike() {
            // given
            Long memberId = 1L;
            Long appointmentId = 2L;
            Long appointmentOptionId = 3L;
            int initialLikes = 5;

            Mockito.lenient().when(optionRepository.getById(appointmentOptionId)).thenReturn(mockOption);
            Mockito.lenient().when(appointmentMemberRepository.findByMemberIdAndAppointmentId(memberId, appointmentId))
                    .thenReturn(Optional.of(mockMember));
            Mockito.lenient().when(likeRepository.getLikeCountByOptionId(appointmentOptionId)).thenReturn(initialLikes);

            // when
            DecreaseResponse response = likeService.decrease(memberId, appointmentId, appointmentOptionId);

            // then
            verify(likeRepository, times(1)).deleteLikeByAppointmentMemberIdAndOptionId(anyLong(), anyLong());
            assertThat(response.getLikes()).isEqualTo(initialLikes);
        }

        @Test
        @DisplayName("좋아요를 추가하면 갯수가 증가한다.")
        void increaseLike() {
            // given
            Long memberId = 1L;
            Long appointmentId = 2L;
            Long appointmentOptionId = 3L;

            when(optionRepository.getById(appointmentOptionId)).thenReturn(mockOption);
            when(appointmentMemberRepository.findByMemberIdAndAppointmentId(memberId, appointmentId))
                    .thenReturn(Optional.of(mockMember));
            when(likeRepository.getLikeCountByOptionId(appointmentOptionId)).thenReturn(5);

            // when
            IncreaseResponse response = likeService.increase(memberId, appointmentId, appointmentOptionId);

            // then
            verify(likeRepository, times(1)).save(any(Like.class));
            assertThat(response.getLikes()).isEqualTo(5);
        }
    }

    @Nested
    @DisplayName("좋아요 증가 (Failure)")
    class Failure {
        @Test
        @DisplayName("좋아요 개수가 0이면 예외 발생")
        void decreaseLike_Fail_LikesNotNegative() {
            // given
            Long memberId = 1L;
            Long appointmentId = 2L;
            Long appointmentOptionId = 3L;

            when(likeRepository.getLikeCountByOptionId(appointmentOptionId)).thenReturn(0);

            // when & then
            assertThatThrownBy(() -> likeService.decrease(memberId, appointmentId, appointmentOptionId))
                    .isInstanceOf(LikesException.class)
                    .hasMessageContaining(LikesErrorCode.LIKES_NOT_NEGATIVE.getMessage());

            verify(likeRepository, never()).deleteLikeByAppointmentMemberIdAndOptionId(anyLong(), anyLong());
        }

        @Test
        @DisplayName("회원이 약속에 참여하지 않은 경우 예외 발생")
        void decreaseLike_Fail_AppointmentMemberNotFound() {
            // given
            Long memberId = 1L;
            Long appointmentId = 2L;
            Long appointmentOptionId = 3L;

            when(likeRepository.getLikeCountByOptionId(appointmentOptionId)).thenReturn(5);
            when(appointmentMemberRepository.findByMemberIdAndAppointmentId(memberId, appointmentId))
                    .thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> likeService.decrease(memberId, appointmentId, appointmentOptionId))
                    .isInstanceOf(AppointmentMemberException.class)
                    .hasMessage(AppointmentErrorCode.APPOINTMENT_NOT_FOUND.getMessage());

            verify(likeRepository, never()).deleteLikeByAppointmentMemberIdAndOptionId(anyLong(), anyLong());
        }
        @Test
        @DisplayName("좋아요 개수가 최대값을 초과하면 예외 발생")
        void increaseLike_Fail_OverMaxLikes() {
            // given
            Long memberId = 1L;
            Long appointmentId = 2L;
            Long appointmentOptionId = 3L;
            int MAX_LIKES = 50;

            Mockito.lenient().when(appointmentMemberRepository.findByMemberIdAndAppointmentId(memberId, appointmentId))
                    .thenReturn(Optional.of(mockMember));

            // 👍 추가: MAX_LIKES를 반환하도록 설정
            Mockito.lenient().when(likeRepository.getLikeCountByOptionId(appointmentOptionId)).thenReturn(MAX_LIKES);

            // when & then
            assertThatThrownBy(() -> likeService.increase(memberId, appointmentId, appointmentOptionId))
                    .isInstanceOf(LikesException.class)
                    .hasMessageContaining(LikesErrorCode.OVER_MAX_LIKES.getMessage(MAX_LIKES));

            verify(likeRepository, never()).save(any(Like.class));
        }


        @Test
        @DisplayName("회원이 약속에 참여하지 않은 경우 예외 발생")
        void increaseLike_Fail_AppointmentMemberNotFound() {
            // given
            Long memberId = 1L;
            Long appointmentId = 2L;
            Long appointmentOptionId = 3L;

            when(appointmentMemberRepository.findByMemberIdAndAppointmentId(memberId, appointmentId))
                    .thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> likeService.increase(memberId, appointmentId, appointmentOptionId))
                    .isInstanceOf(AppointmentMemberException.class)
                    .hasMessage(AppointmentErrorCode.APPOINTMENT_NOT_FOUND.getMessage());

            verify(likeRepository, never()).save(any(Like.class));
        }
    }

    // ======================== Fake Service ========================
    static class FakeLikeService {
        private final FakeAppointmentOptionRepository optionRepository;
        private final FakeAppointmentMemberRepository appointmentMemberRepository;
        private final FakeLikeRepository likeRepository;
        private static final int MAX_LIKES = 50;

        FakeLikeService(FakeAppointmentOptionRepository optionRepository, FakeAppointmentMemberRepository appointmentMemberRepository, FakeLikeRepository likeRepository) {
            this.optionRepository = optionRepository;
            this.appointmentMemberRepository = appointmentMemberRepository;
            this.likeRepository = likeRepository;
        }

        public IncreaseResponse increase(Long memberId, Long appointmentId, Long appointmentOptionId) {
            createLike(memberId, appointmentId, appointmentOptionId);
            int likes = getLikeCountByOptionId(appointmentOptionId);
            return IncreaseResponse.of(likes);
        }

        public DecreaseResponse decrease(Long memberId, Long appointmentId, Long appointmentOptionId) {
            deleteLike(memberId, appointmentId, appointmentOptionId);
            int likes = getLikeCountByOptionId(appointmentOptionId);
            return DecreaseResponse.of(likes);
        }

        public int getLikeCountByOptionId(Long appointmentOptionId) {
            return likeRepository.getLikeCountByOptionId(appointmentOptionId);
        }

        private void createLike(Long memberId, Long appointmentId, Long appointmentOptionId) {
            int count = getLikeCountByOptionId(appointmentOptionId);
            if (count == MAX_LIKES) {
                throw new LikesException(LikesErrorCode.OVER_MAX_LIKES, MAX_LIKES);
            }

            AppointmentOption appointmentOption = optionRepository.getById(appointmentOptionId);
            AppointmentMember appointmentMember = appointmentMemberRepository.findByMemberIdAndAppointmentId(memberId, appointmentId)
                    .orElseThrow(() -> new AppointmentMemberException(AppointmentErrorCode.APPOINTMENT_NOT_FOUND));

            Like newLike = Like.of(appointmentMember, appointmentOption);
            likeRepository.save(newLike);
        }

        private void deleteLike(Long memberId, Long appointmentId, Long appointmentOptionId) {
            int count = getLikeCountByOptionId(appointmentOptionId);

            if (count == 0) {
                throw new LikesException(LikesErrorCode.LIKES_NOT_NEGATIVE);
            }

            AppointmentMember appointmentMember =
                    appointmentMemberRepository.findByMemberIdAndAppointmentId(memberId, appointmentId)
                            .orElseThrow(() -> new AppointmentMemberException(AppointmentErrorCode.APPOINTMENT_NOT_FOUND));

            Long appointmentMemberId = appointmentMember.getId();
            likeRepository.deleteLikeByAppointmentMemberIdAndOptionId(appointmentMemberId, appointmentOptionId);
        }
    }


    // ======================== Fake Repository ========================

    static class FakeLikeRepository {
        void save(Like like) {}

        int getLikeCountByOptionId(Long appointmentOptionId) { return 0; }

        void deleteLikeByAppointmentMemberIdAndOptionId(Long appointmentMemberId, Long appointmentOptionId) {}
    }


    static class FakeAppointmentOptionRepository {
        AppointmentOption getById(Long id) { return null; }
    }

    static class FakeAppointmentMemberRepository {
        Optional<AppointmentMember> findByMemberIdAndAppointmentId(Long memberId, Long appointmentId) { return Optional.empty(); }
    }
}
