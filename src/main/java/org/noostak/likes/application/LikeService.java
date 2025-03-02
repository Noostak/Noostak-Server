package org.noostak.likes.application;

import org.noostak.likes.dto.DecreaseResponse;
import org.noostak.likes.dto.IncreaseResponse;

public interface LikeService {
    IncreaseResponse increase(Long memberId, Long appointmentId, Long appointmentOptionId);

    DecreaseResponse decrease(Long memberId, Long appointmentId, Long appointmentOptionId);

    Long getLikeCountByOptionId(Long optionId);
}
