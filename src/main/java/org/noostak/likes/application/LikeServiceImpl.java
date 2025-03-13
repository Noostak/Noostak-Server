package org.noostak.likes.application;


import lombok.RequiredArgsConstructor;
import org.noostak.appointmentmember.domain.AppointmentMember;
import org.noostak.appointmentmember.domain.AppointmentMemberRepository;
import org.noostak.appointmentoption.domain.AppointmentOption;
import org.noostak.appointmentoption.domain.AppointmentOptionRepository;
import org.noostak.likes.common.exception.LikesErrorCode;
import org.noostak.likes.common.exception.LikesException;
import org.noostak.likes.domain.Like;
import org.noostak.likes.domain.LikeRepository;
import org.noostak.likes.dto.DecreaseResponse;
import org.noostak.likes.dto.IncreaseResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeServiceImpl implements LikeService {

    private final static int MAX_LIKES = 50;
    private final AppointmentOptionRepository optionRepository;
    private final AppointmentMemberRepository appointmentMemberRepository;
    private final LikeRepository likeRepository;

    @Override
    @Transactional
    public IncreaseResponse increase(Long memberId, Long appointmentId, Long appointmentOptionId) {
        long likes = createLike(memberId, appointmentId, appointmentOptionId);
        return IncreaseResponse.of(likes);
    }

    @Override
    @Transactional
    public DecreaseResponse decrease(Long memberId, Long appointmentId, Long appointmentOptionId) {
        long likes = deleteLike(memberId, appointmentId, appointmentOptionId);
        return DecreaseResponse.of(likes);
    }

    @Override
    public Long getLikeCountByOptionId(Long appointmentOptionId) {
        return likeRepository.getLikeCountByOptionId(appointmentOptionId);
    }


    private long deleteLike(Long memberId, Long appointmentId, Long appointmentOptionId) {
        long currentCount = getLikeCountByOptionId(appointmentOptionId);

        if (currentCount == 0) {
            throw new LikesException(LikesErrorCode.LIKES_NOT_NEGATIVE);
        }

        AppointmentMember appointmentMember =
                appointmentMemberRepository.getByMemberIdAndAppointmentId(memberId, appointmentId);

        Long appointmentMemberId = appointmentMember.getId();

        likeRepository.deleteLikeByAppointmentMemberIdAndOptionId(appointmentMemberId, appointmentOptionId);

        return currentCount - 1;
    }

    private long createLike(Long memberId, Long appointmentId, Long appointmentOptionId) {
        long currentCount = getLikeCountByOptionId(appointmentOptionId);

        if (currentCount == MAX_LIKES) {
            throw new LikesException(LikesErrorCode.OVER_MAX_LIKES, MAX_LIKES);
        }

        AppointmentOption appointmentOption = optionRepository.getByAppointmentOptionId(appointmentId);

        AppointmentMember appointmentMember =
                appointmentMemberRepository.getByMemberIdAndAppointmentId(memberId, appointmentId);

        Like newLike = Like.of(appointmentMember, appointmentOption);

        likeRepository.save(newLike);

        return currentCount + 1;
    }
}
