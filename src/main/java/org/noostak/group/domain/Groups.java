package org.noostak.group.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.noostak.global.entity.BaseTimeEntity;
import org.noostak.group.domain.vo.GroupInvitationCode;
import org.noostak.group.domain.vo.GroupMemberCount;
import org.noostak.group.domain.vo.GroupName;
import org.noostak.group.domain.vo.GroupProfileImageKey;

@Entity
@Getter
@RequiredArgsConstructor
public class Groups extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;

    @Embedded
    @AttributeOverride(name = "code", column = @Column(name = "group_invitation_code"))
    GroupInvitationCode code;

    @Embedded
    @AttributeOverride(name = "name", column = @Column(name = "group_name"))
    private GroupName name;

    @Embedded
    @AttributeOverride(name = "key", column = @Column(name = "group_profile_image_key"))
    private GroupProfileImageKey key;

    @Embedded
    @AttributeOverride(name = "count", column = @Column(name = "group_member_count"))
    private GroupMemberCount memberCount;

    private Long groupHostId;

    private Groups(final Long groupHostId, final GroupName name, final GroupProfileImageKey key, final String code) {
        this.groupHostId = groupHostId;
        this.name = name;
        this.key = key;
        this.memberCount = GroupMemberCount.from(1L);
        this.code = GroupInvitationCode.from(code);
    }

    public static Groups of(final Long groupHostId, final GroupName name, final GroupProfileImageKey key, final String code) {
        return new Groups(groupHostId, name, key, code);
    }
}
