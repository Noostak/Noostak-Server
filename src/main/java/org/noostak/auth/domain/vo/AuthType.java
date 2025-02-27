package org.noostak.auth.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.noostak.auth.common.exception.AuthErrorCode;
import org.noostak.auth.common.exception.AuthException;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum AuthType {
    KAKAO("KAKAO"),
    GOOGLE("GOOGLE"),

    ;

    private final String name;

    public static AuthType from(String givenAuthType){
        return Arrays.stream(AuthType.values())
                .filter(type->type.name().equals(givenAuthType)).findFirst()
                .orElseThrow(()->new AuthException(AuthErrorCode.AUTH_TYPE_NOT_EXISTS,givenAuthType));
    }
}
