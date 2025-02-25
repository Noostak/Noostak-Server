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

        int likes = getLikeCountByOptionId(appointmentOptionId);
        return IncreaseResponse.of(likes+1);
    }

    @Override
    public int getLikeCountByOptionId(Long appointmentOptionId) {
        return likeRepository.getLikeCountByOptionId(appointmentOptionId);
    }


    private void createLike(Long memberId, Long appointmentId, Long appointmentOptionId) {

        int count = getLikeCountByOptionId(appointmentOptionId);

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
