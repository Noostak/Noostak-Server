package org.noostak.membergroup.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.noostak.group.domain.Group;
import org.noostak.member.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.noostak.member.domain.QMember.member;
import static org.noostak.group.domain.QGroup.group;
import static org.noostak.membergroup.domain.QMemberGroup.memberGroup;

@Repository
@RequiredArgsConstructor
public class MemberGroupRepositoryImpl implements MemberGroupRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Member> findMembersByGroupId(Long groupId) {
        return queryFactory
                .select(member)
                .from(memberGroup)
                .join(memberGroup.member, member)
                .where(memberGroup.group.id.eq(groupId))
                .fetch();
    }

    @Override
    public Member findGroupHostByGroupId(Long groupId) {
        return queryFactory
                .select(member)
                .from(group)
                .join(member).on(group.groupHostId.eq(member.id))
                .where(group.id.eq(groupId))
                .fetchOne();
    }

    @Override
    public List<Group> findGroupsByMemberId(Long memberId) {
        return queryFactory
                .select(memberGroup.group)
                .from(memberGroup)
                .where(memberGroup.member.id.eq(memberId))
                .fetch();
    }

    @Override
    public boolean existsByMemberIdAndGroupId(Long memberId, Long groupId) {
        Integer fetchResult = queryFactory
                .selectOne()
                .from(memberGroup)
                .where(
                        memberGroup.member.id.eq(memberId)
                                .and(memberGroup.group.id.eq(groupId))
                )
                .fetchFirst();

        return fetchResult != null;
    }
}
