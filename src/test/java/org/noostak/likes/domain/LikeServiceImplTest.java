package org.noostak.likes.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.noostak.appointment.common.exception.AppointmentErrorCode;
import org.noostak.appointmentmember.common.exception.AppointmentMemberException;
import org.noostak.appointmentmember.domain.AppointmentMember;
import org.noostak.appointmentoption.domain.AppointmentOption;
import org.noostak.likes.dto.IncreaseResponse;

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
    @DisplayName("좋아요 증가 (Success)")
    class Success {

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

        public int getLikeCountByOptionId(Long appointmentOptionId) {
            return likeRepository.getLikeCountByOptionId(appointmentOptionId);
        }

        private void createLike(Long memberId, Long appointmentId, Long appointmentOptionId) {
            AppointmentOption appointmentOption = optionRepository.getById(appointmentOptionId);
            AppointmentMember appointmentMember = appointmentMemberRepository.findByMemberIdAndAppointmentId(memberId, appointmentId)
                    .orElseThrow(() -> new AppointmentMemberException(AppointmentErrorCode.APPOINTMENT_NOT_FOUND));

            Like newLike = Like.of(appointmentMember, appointmentOption);
            likeRepository.save(newLike);
        }
    }

    // ======================== Fake Repository ========================

    static class FakeLikeRepository {
        void save(Like like) {}
        int getLikeCountByOptionId(Long appointmentOptionId) { return 0; }
    }

    static class FakeAppointmentOptionRepository {
        AppointmentOption getById(Long id) { return null; }
    }

    static class FakeAppointmentMemberRepository {
        Optional<AppointmentMember> findByMemberIdAndAppointmentId(Long memberId, Long appointmentId) { return Optional.empty(); }
    }
}
