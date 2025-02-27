package org.noostak.likes.domain;

public interface LikeRepositoryCustom {

    Long getLikeCountByOptionId(Long optionId);

    void deleteLikeByAppointmentMemberIdAndOptionId(Long appointmentMemberId, Long optionId);

    boolean getExistsByAppointmentOptionIdAndAppointmentMemberId(Long appointmentOptionId, Long appointmentMemberId);
}
