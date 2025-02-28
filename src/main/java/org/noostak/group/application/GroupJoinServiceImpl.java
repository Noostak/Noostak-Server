package org.noostak.group.application;

import lombok.RequiredArgsConstructor;
import org.noostak.group.domain.Group;
import org.noostak.group.domain.GroupRepository;
import org.noostak.group.dto.response.GroupJoinResponse;
import org.noostak.member.domain.Member;
import org.noostak.member.domain.MemberRepository;
import org.noostak.membergroup.domain.MemberGroup;
import org.noostak.membergroup.domain.MemberGroupRepository;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class GroupJoinServiceImpl implements GroupJoinService{
    private final GroupRepository groupRepository;
    private final MemberGroupRepository memberGroupRepository;
    private final MemberRepository memberRepository;
    @Override
    public GroupJoinResponse join(Long memberId, String inviteCode) {
        Member member = memberRepository.getById(memberId);
        Group group = groupRepository.findByCode(inviteCode);

        MemberGroup memberGroup = MemberGroup.of(member,group);

        memberGroupRepository.save(memberGroup);

        return GroupJoinResponse.of(group.getId());
    }
}
