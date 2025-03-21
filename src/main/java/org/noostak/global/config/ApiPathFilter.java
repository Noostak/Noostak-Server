//package org.noostak.global.config;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.noostak.global.utils.GlobalLogger;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//public class ApiPathFilter extends OncePerRequestFilter {
//
//    private static final String API_PATTERN = "/api/v1";
//    private static final String HEALTH_PATTERN = "/actuator/health";
//    private static final String SWAGGER_PATTERN = "/swagger-ui";
//
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//        String requestUri = request.getRequestURI();
//
//        GlobalLogger.info("[Filter] 요청 호스트 정보:",request.getRemoteHost(),request.getRemotePort());
//        GlobalLogger.info("[Filter] 요청 경로 정보:",request.getMethod(),request.getRequestURI());
//
//        if (isApiRequest(requestUri) || isHealthCheck(requestUri) || isSwaggerRequest(requestUri)) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        response.sendError(HttpServletResponse.SC_FORBIDDEN);
//    }
//
//    private boolean isHealthCheck(String requestUri){
//        return requestUri.startsWith(HEALTH_PATTERN);
//    }
//    private boolean isApiRequest(String requestUri) {
//        return requestUri.startsWith(API_PATTERN);
//    }
//
//    private boolean isSwaggerRequest(String requestUri) {
//        return requestUri.startsWith(SWAGGER_PATTERN);
//    }
//}