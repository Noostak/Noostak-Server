package org.noostak.auth.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.noostak.auth.domain.vo.AuthId;
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
    private AuthId authId;

    @Embedded
    @AttributeOverride(name = "token", column = @Column(name = "refresh_token"))
    private RefreshToken refreshToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void setRefreshToken(RefreshToken refreshToken){
        this.refreshToken = refreshToken;
    }

    private AuthInfo(AuthType authType, AuthId authId, RefreshToken refreshToken, Member member) {
        this.authType = authType;
        this.authId = authId;
        this.refreshToken = refreshToken;
        this.member = member;
    }

    public static AuthInfo of(AuthType type, AuthId authId, RefreshToken token, Member member) {
        return new AuthInfo(type,authId,token,member);
    }
}
