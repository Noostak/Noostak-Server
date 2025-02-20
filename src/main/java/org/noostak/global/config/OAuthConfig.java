package org.noostak.global.config;

import org.noostak.auth.application.RestClient;
import org.noostak.global.KakaoTokenRequestFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OAuthConfig {
    @Value("${oauth-property.kakao.client_id}")
    private String kakaoClientId;

    @Value("${oauth-property.kakao.redirect_uri}")
    private String kakaoRedirectUri;

    @Bean
    public KakaoTokenRequestFactory kakaoTokenRequestFactory() {
        return new KakaoTokenRequestFactory(kakaoClientId, kakaoRedirectUri);
    }

    @Bean
    public RestClient restClient(){
        return new RestClient();
    }
}