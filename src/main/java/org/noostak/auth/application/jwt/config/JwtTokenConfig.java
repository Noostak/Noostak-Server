package org.noostak.auth.application.jwt.config;

import org.noostak.auth.application.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtTokenConfig {

    @Value("${jwt.secret}")
    private String secretKey;

    @Bean
    public JwtTokenProvider jwtTokenProvider(String secretKey) {
        return new JwtTokenProvider(secretKey);
    }
}
