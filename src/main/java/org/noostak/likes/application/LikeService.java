package org.noostak.likes.application;

import org.noostak.likes.dto.IncreaseResponse;

public interface LikeService {
    IncreaseResponse increase(Long memberId, Long appointmentId, Long appointmentOptionId);
    int getLikeCountByOptionId(Long optionId);
}
