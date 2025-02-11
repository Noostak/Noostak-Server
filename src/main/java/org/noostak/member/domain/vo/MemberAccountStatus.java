package org.noostak.member.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberAccountStatus {
    ACTIVE("활성"),
    INACTIVE("비활성");

    private final String message;
}
