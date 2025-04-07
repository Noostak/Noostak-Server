package org.noostak.like.application;


import lombok.RequiredArgsConstructor;
import org.noostak.appointment.common.exception.AppointmentErrorCode;
import org.noostak.appointment.common.exception.AppointmentException;
import org.noostak.appointmentmember.domain.AppointmentMember;
import org.noostak.appointmentmember.domain.AppointmentMemberRepository;
import org.noostak.appointmentoption.domain.AppointmentOption;
import org.noostak.appointmentoption.domain.AppointmentOptionRepository;
import org.noostak.like.common.exception.LikesErrorCode;
import org.noostak.like.common.exception.LikesException;
import org.noostak.like.domain.Like;
import org.noostak.like.domain.LikeRepository;
import org.noostak.like.dto.DecreaseResponse;
import org.noostak.like.dto.IncreaseResponse;
import org.noostak.member.domain.Member;
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
        long likes = increaseLike(memberId, appointmentId, appointmentOptionId);
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

        // TODO: 입력받은 appointmentId와 옵션에 있는 appointmentId가 다를 때

        AppointmentMember appointmentMember =
                appointmentMemberRepository.getByMemberIdAndAppointmentId(memberId, appointmentId);

        Long appointmentMemberId = appointmentMember.getId();

        likeRepository.deleteLikeByAppointmentMemberIdAndOptionId(appointmentMemberId, appointmentOptionId);

        return currentCount - 1;
    }

    private long increaseLike(Long memberId, Long appointmentId, Long appointmentOptionId) {
        long currentCount = getLikeCountByOptionId(appointmentOptionId);

        if (currentCount == MAX_LIKES) {
            throw new LikesException(LikesErrorCode.OVER_MAX_LIKES, MAX_LIKES);
        }

        Like newLike = createLike(memberId,appointmentId,appointmentOptionId);

        if(hasLike(newLike)){
            return currentCount;
        }

        likeRepository.save(newLike);

        return currentCount + 1;
    }

    private Like createLike(Long memberId, Long appointmentId, Long appointmentOptionId){
        AppointmentOption appointmentOption = optionRepository.getByAppointmentOptionId(appointmentOptionId);

        // 입력받은 appointmentId와 옵션에 있는 appointmentId가 다를 때
        if(!appointmentOption.getAppointment().getId().equals(appointmentId)){
            throw new AppointmentException(AppointmentErrorCode.MEMBER_NOT_CONTAINED);
        }

        AppointmentMember appointmentMember =
                appointmentMemberRepository.getByMemberIdAndAppointmentId(memberId, appointmentId);

        return Like.of(appointmentMember, appointmentOption);
    }

    private boolean hasLike(Like like){
        Member member = like.getAppointmentMember().getMember();
        AppointmentOption option = like.getAppointmentOption();

        return likeRepository.
                getExistsByAppointmentOptionIdAndMemberId(option.getId(),member.getId());
    }
}
