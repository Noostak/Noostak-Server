package org.noostak.auth.application;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.noostak.auth.common.exception.AuthErrorCode;
import org.noostak.auth.common.exception.AuthException;
import org.noostak.auth.domain.vo.AuthType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OauthServiceFactory {
    private final Map<String, OauthService> serviceMap = new HashMap<>();
    private final KakaoService kakaoService;
    private final GoogleService googleService;

    @PostConstruct
    public void init() {
        serviceMap.put(AuthType.KAKAO.getName(), kakaoService);
        serviceMap.put(AuthType.GOOGLE.getName(), googleService);
    }

    public OauthService getService(String authType) {
        if(!serviceMap.containsKey(authType)){
            throw new AuthException(AuthErrorCode.AUTH_TYPE_NOT_EXISTS, authType);
        }

        return serviceMap.get(authType);
    }

    public OauthService getService(AuthType authType) {
        OauthService service = serviceMap.get(authType.getName());

        if(service == null){
            throw new AuthException(AuthErrorCode.AUTH_TYPE_NOT_EXISTS, authType);
        }

        return serviceMap.get(authType.getName());
    }
}
