package org.noostak.member.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.noostak.global.entity.BaseTimeEntity;
import org.noostak.member.domain.vo.*;

@Entity
@Getter
@RequiredArgsConstructor
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Embedded
    @AttributeOverride(name = "name", column = @Column(name = "member_name"))
    private MemberName name;

    @Embedded
    @AttributeOverride(name = "key", column = @Column(name = "member_profile_image_key"))
    private MemberProfileImageKey key;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_account_status")
    private MemberAccountStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_type")
    private AuthType type;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "auth_id"))
    private AuthId id;

    private String refreshToken;
}
