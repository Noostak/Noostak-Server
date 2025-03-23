package org.noostak.membergroup.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.noostak.global.entity.BaseTimeEntity;
import org.noostak.group.domain.Group;
import org.noostak.member.domain.Member;

@Entity
@Getter
@NoArgsConstructor
public class MemberGroup extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_group_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Group group;

    private MemberGroup(final Member member, final Group groups) {
        this.member = member;
        this.group = groups;
    }

    public static MemberGroup of(final Member member, final Group group) {
        return new MemberGroup(member, group);
    }
}
