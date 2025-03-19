package org.noostak.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns(
                        "/api/v1/auth/sign-in",       // 로그인 제외
                        "/api/v1/auth/sign-up",       // 회원가입 제외
                        "/api/v1/auth/token-reissue", // 토큰 재발급 제외
                        "/api/v1/auth/logout"         // 로그아웃 제외
                );
    }
}

