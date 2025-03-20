//package org.noostak.global.config;
//
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.Ordered;
//
//@Configuration
//public class FilterConfig {
//
//    @Bean
//    public FilterRegistrationBean<ApiPathFilter> staticResourceFilter() {
//        FilterRegistrationBean<ApiPathFilter> registrationBean = new FilterRegistrationBean<>();
//        registrationBean.setFilter(new ApiPathFilter());
//        registrationBean.addUrlPatterns("/*");
//        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
//
//        return registrationBean;
//    }
//}