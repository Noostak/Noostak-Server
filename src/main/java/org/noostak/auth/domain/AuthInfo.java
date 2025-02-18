package org.noostak.auth.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.noostak.auth.domain.vo.Code;
import org.noostak.auth.domain.vo.AuthType;
import org.noostak.auth.domain.vo.RefreshToken;
import org.noostak.member.domain.Member;


@Entity
@Getter
@RequiredArgsConstructor
public class AuthInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authInfoId;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_type")
    private AuthType authType;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "auth_id"))
    private Code code;

    @Embedded
    @AttributeOverride(name = "token", column = @Column(name = "refresh_token"))
    private RefreshToken refreshToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private AuthInfo(AuthType authType, Code code, RefreshToken refreshToken, Member member) {
        this.authType = authType;
        this.code = code;
        this.refreshToken = refreshToken;
        this.member = member;
    }

    public static AuthInfo of(AuthType type, Code id, RefreshToken token, Member member) {
        return new AuthInfo(type,id,token,member);
    }
}
