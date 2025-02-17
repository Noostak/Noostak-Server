package org.noostak.membergroup.domain;

import org.noostak.group.domain.Group;
import org.noostak.member.domain.Member;
import java.util.List;

public interface MemberGroupRepositoryCustom {
    List<Member> findMembersByGroupId(Long groupId);
    Member findGroupHostByGroupId(Long groupId);
    List<Group> findGroupsByMemberId(Long memberId);
}
