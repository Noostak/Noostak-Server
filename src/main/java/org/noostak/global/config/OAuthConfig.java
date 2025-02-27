package org.noostak.global.config;

import org.noostak.auth.application.AuthTokenResolver;
import org.noostak.auth.application.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class OAuthConfig {

    @Bean
    public RestClient restClient() {
        return new RestClient();
    }

    @Bean
    public AuthTokenResolver authTokenResolver() {
        return new AuthTokenResolver();
    }
}