package org.noostak.likes.application;


import lombok.RequiredArgsConstructor;
import org.noostak.appointment.common.exception.AppointmentErrorCode;
import org.noostak.appointmentmember.common.exception.AppointmentMemberException;
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
public class LikeServiceImpl implements LikeService{

    private final static int MAX_LIKES = 50;
    private final AppointmentOptionRepository optionRepository;
    private final AppointmentMemberRepository appointmentMemberRepository;
    private final LikeRepository likeRepository;

    @Override
    @Transactional
    public IncreaseResponse increase(Long memberId, Long appointmentId, Long appointmentOptionId) {
        createLike(memberId, appointmentId, appointmentOptionId);

        long likes = getLikeCountByOptionId(appointmentOptionId);
        return IncreaseResponse.of(likes);
    }

    @Override
    @Transactional
    public DecreaseResponse decrease(Long memberId, Long appointmentId, Long appointmentOptionId) {
        deleteLike(memberId, appointmentId, appointmentOptionId);

        long likes = getLikeCountByOptionId(appointmentOptionId);
        return DecreaseResponse.of(likes);
    }

    @Override
    public Long getLikeCountByOptionId(Long appointmentOptionId) {
        return likeRepository.getLikeCountByOptionId(appointmentOptionId);
    }


    private void deleteLike(Long memberId, Long appointmentId, Long appointmentOptionId) {

        long count = getLikeCountByOptionId(appointmentOptionId);

        if(count == 0){
            throw new LikesException(LikesErrorCode.LIKES_NOT_NEGATIVE);
        }

        AppointmentMember appointmentMember =
                appointmentMemberRepository
                        .findByMemberIdAndAppointmentId(memberId, appointmentId)
                        .orElseThrow(()->new AppointmentMemberException(AppointmentErrorCode.APPOINTMENT_NOT_FOUND));

        Long appointmentMemberId = appointmentMember.getId();

        likeRepository.deleteLikeByAppointmentMemberIdAndOptionId(appointmentMemberId,appointmentOptionId);
    }

    private void createLike(Long memberId, Long appointmentId, Long appointmentOptionId) {

        long count = getLikeCountByOptionId(appointmentOptionId);

        if(count == MAX_LIKES){
            throw new LikesException(LikesErrorCode.OVER_MAX_LIKES,MAX_LIKES);
        }

        AppointmentOption appointmentOption = optionRepository.getById(appointmentOptionId);
        AppointmentMember appointmentMember =
                appointmentMemberRepository
                        .findByMemberIdAndAppointmentId(memberId, appointmentId)
                        .orElseThrow(()->new AppointmentMemberException(AppointmentErrorCode.APPOINTMENT_NOT_FOUND));

        Like newLike = Like.of(appointmentMember, appointmentOption);

        likeRepository.save(newLike);
    }
}
