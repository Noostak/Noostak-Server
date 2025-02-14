package org.noostak.membergroup.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.noostak.global.entity.BaseTimeEntity;
import org.noostak.group.domain.Group;
import org.noostak.member.domain.Member;

@Entity
@Getter
@NoArgsConstructor
public class MemberGroups extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberGroupId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    private MemberGroups(final Member member, final Group groups) {
        this.member = member;
        this.group = groups;
    }

    public static MemberGroups of(final Member member, final Group group) {
        return new MemberGroups(member, group);
    }
}
