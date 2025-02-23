package org.noostak.auth.application;


import lombok.Getter;
import org.noostak.auth.application.jwt.JwtToken;
import org.noostak.auth.common.exception.AuthErrorCode;
import org.noostak.auth.common.exception.AuthException;
import org.noostak.auth.domain.vo.AuthId;
import org.noostak.global.utils.GlobalLogger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthTokenResolver {

    private static final String PREFIX = "[AuthTokenResolver] ";

    private static final String NOT_EXISTS_MESSAGE = PREFIX + "저장된 토큰 정보가 없습니다. 입력값:";

    private static final String DELETE_MESSAGE = PREFIX + "다음의 AuthId가 삭제됩니다. :";

    private static final int EXPIRED_AT = 3600;

    private final Map<AuthId, JwtTokenWithExpireDate> map;


    public AuthTokenResolver() {
        this.map = new HashMap<>();
    }


    @Scheduled(cron = "0 0 * * * *") // 매 정각마다 실행
    public void deleteAllIfExpired() {
        map.keySet().stream()
                .filter(authId -> map.get(authId).isExpired())
                .forEach(this::delete);
    }


    public void delete(AuthId authId) {
        if (!isExists(authId)) {
            return;
        }

        GlobalLogger.warn(DELETE_MESSAGE, authId.value());

        map.remove(authId);
    }

    public void put(AuthId authId, JwtToken jwtToken) {
        if(authId == null){
            throw new AuthException(AuthErrorCode.AUTH_ID_NOT_EXISTS,authId.value());
        }

        map.put(authId, JwtTokenWithExpireDate.of(jwtToken, EXPIRED_AT));
    }

    public JwtToken get(AuthId authId) {
        if (!isExists(authId)) {
            throw new AuthException(AuthErrorCode.AUTH_ID_NOT_EXISTS, authId.value());
        }

        JwtTokenWithExpireDate token = map.get(authId);

        if (token.isExpired()) {
            delete(authId);
            throw new AuthException(AuthErrorCode.EXPIRED_AUTH);
        }

        return token.getJwtToken();
    }

    public boolean isExists(AuthId authId) {
        boolean isExists = map.containsKey(authId);

        if (!isExists) {
            GlobalLogger.warn(NOT_EXISTS_MESSAGE, authId.value());
        }

        return isExists;
    }
}

@Getter
class JwtTokenWithExpireDate {
    private final JwtToken jwtToken;
    private final LocalDateTime expireDate;

    private JwtTokenWithExpireDate(JwtToken jwtToken, int expiredAt) {
        this.jwtToken = jwtToken;
        this.expireDate = LocalDateTime.now().plusSeconds(expiredAt);
    }

    public static JwtTokenWithExpireDate of(JwtToken jwtToken, int expiredAt) {
        return new JwtTokenWithExpireDate(jwtToken, expiredAt);
    }

    public boolean isExpired() {
        return expireDate.isBefore(LocalDateTime.now());
    }
}
