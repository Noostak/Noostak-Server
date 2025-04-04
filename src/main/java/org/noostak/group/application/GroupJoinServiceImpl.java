package org.noostak.group.application;

import lombok.RequiredArgsConstructor;
import org.noostak.appointment.domain.Appointment;
import org.noostak.appointment.domain.AppointmentRepository;
import org.noostak.appointmentmember.application.AppointmentMemberSaveService;
import org.noostak.group.common.exception.GroupErrorCode;
import org.noostak.group.common.exception.GroupException;
import org.noostak.group.domain.Group;
import org.noostak.group.domain.GroupRepository;
import org.noostak.group.dto.response.GroupJoinResponse;
import org.noostak.member.domain.Member;
import org.noostak.member.domain.MemberRepository;
import org.noostak.membergroup.domain.MemberGroup;
import org.noostak.membergroup.domain.MemberGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class GroupJoinServiceImpl implements GroupJoinService {

    private final GroupRepository groupRepository;
    private final MemberGroupRepository memberGroupRepository;
    private final MemberRepository memberRepository;
    private final AppointmentRepository appointmentRepository;
    private final AppointmentMemberSaveService appointmentMemberSaveService;

    @Override
    @Transactional
    public GroupJoinResponse join(Long memberId, String inviteCode) {
        Member member = memberRepository.getById(memberId);
        Group group = groupRepository.findByCode(inviteCode);

        validate(group,member);

        MemberGroup memberGroup = MemberGroup.of(member, group);
        memberGroupRepository.save(memberGroup);

        // 그룹 내 진행 중인 약속에 대해, 멤버-약속 관계 형성
        saveAppointmentMemberInProgress(member, group);

        // 그룹 멤버 수 추가
        Long countedInGroup = memberGroupRepository.countAllByGroup(group);
        group.setCount(countedInGroup);

        return GroupJoinResponse.of(group.getId());
    }


    private void saveAppointmentMemberInProgress(Member member, Group group) {
        appointmentRepository.findAllByGroupId(group.getId())
                .stream().filter(Appointment::inProgress)
                .forEach(appointment ->
                        appointmentMemberSaveService.saveAppointmentMember(appointment, member));
    }


    private void validate(Group group, Member member){
        if(memberGroupRepository.existsByGroupAndMember(group,member)){
            throw new GroupException(GroupErrorCode.GROUP_ALREADY_EXISTS);
        }
    }
}
