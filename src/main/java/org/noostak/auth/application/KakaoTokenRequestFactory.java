package org.noostak.auth.application;

import lombok.ToString;
import org.noostak.auth.dto.KakaoAccessTokenRequest;
import org.noostak.auth.dto.KakaoLogoutRequest;
import org.noostak.auth.dto.KakaoTokenRequest;
import org.noostak.global.utils.GlobalLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
@ToString
public class KakaoTokenRequestFactory {

    private final String clientId;


    private final String redirectUri;


    private final String clientSecret;


    private final String logoutRedirectUri;


    public KakaoTokenRequestFactory(@Value("${oauth-property.kakao.client_id}") String clientId,
                                    @Value("${oauth-property.kakao.redirect_uri}") String redirectUri,
                                    @Value("${oauth-property.kakao.client_secret}") String clientSecret,
                                    @Value("${oauth-property.kakao.logout_redirect_uri}") String logoutRedirectUri) {
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.clientSecret = clientSecret;
        this.logoutRedirectUri = logoutRedirectUri;

        GlobalLogger.info("RequestFactory :",this);
    }

    public KakaoLogoutRequest createLogoutRequest(){
        return KakaoLogoutRequest.of(clientId,logoutRedirectUri);
    }

    public KakaoTokenRequest createRequest(String code) {
        return KakaoTokenRequest.of(clientId, redirectUri, code, clientSecret);
    }

    public KakaoAccessTokenRequest createAccessTokenRequest(String refreshToken) {
        return KakaoAccessTokenRequest.of(clientId, refreshToken, clientSecret);
    }
}