package org.noostak.membergroup.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.noostak.global.entity.BaseTimeEntity;
import org.noostak.group.domain.Groups;
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
    private Groups group;

    private MemberGroups(final Member member, final Groups groups) {
        this.member = member;
        this.group = groups;
    }

    public static MemberGroups of(final Member member, final Groups group) {
        return new MemberGroups(member, group);
    }
}
