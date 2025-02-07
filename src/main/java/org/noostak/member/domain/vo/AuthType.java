package org.noostak.member.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthType {
    KAKAO("카카오"),
    GOOGLE("구글"),
    APPLE("애플");

    private final String message;
}
