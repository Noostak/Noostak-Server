package org.noostak.global.config;

import org.noostak.auth.application.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OAuthConfig {

    @Bean
    public RestClient restClient(){
        return new RestClient();
    }
}