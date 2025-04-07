package org.noostak.like.application;

import org.noostak.like.dto.DecreaseResponse;
import org.noostak.like.dto.IncreaseResponse;

public interface LikeService {
    IncreaseResponse increase(Long memberId, Long appointmentId, Long appointmentOptionId);

    DecreaseResponse decrease(Long memberId, Long appointmentId, Long appointmentOptionId);

    Long getLikeCountByOptionId(Long optionId);
}
