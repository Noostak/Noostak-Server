package org.noostak.membergroup.domain;

import org.noostak.group.domain.Group;
import org.noostak.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberGroupRepository extends JpaRepository<MemberGroup, Long>, MemberGroupRepositoryCustom{
    List<MemberGroup> findByMemberId(Long memberId);

    boolean existsByGroupAndMember(Group group, Member member);

    Long countAllByGroup(Group group);
}
