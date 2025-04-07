package org.noostak.like.domain;

public interface LikeRepositoryCustom {

    Long getLikeCountByOptionId(Long optionId);

    void deleteLikeByAppointmentMemberIdAndOptionId(Long appointmentMemberId, Long optionId);

    boolean getExistsByAppointmentOptionIdAndMemberId(Long appointmentOptionId, Long memberId);
}
