package org.noostak.likes.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static org.noostak.likes.domain.QLike.like;

@RequiredArgsConstructor
public class LikeRepositoryImpl implements LikeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Long getLikeCountByOptionId(Long optionId) {
        return queryFactory
                .select(like.count())
                .from(like)
                .where(like.appointmentOption.id.eq(optionId))
                .fetchOne();
    }

    @Override
    public void deleteLikeByAppointmentMemberIdAndOptionId(Long appointmentMemberId, Long optionId) {
        queryFactory.delete(like)
                .where(like.appointmentMember.id.eq(appointmentMemberId),
                        like.appointmentOption.id.eq(optionId))
                .execute();
    }

    @Override
    public boolean getExistsByAppointmentOptionIdAndAppointmentMemberId(Long appointmentOptionId, Long appointmentMemberId) {
        Integer result = queryFactory
                .selectOne()
                .from(like)
                .where(like.appointmentOption.id.eq(appointmentOptionId),
                        like.appointmentMember.id.eq(appointmentMemberId))
                .fetchFirst();
        return result != null;
    }
}
