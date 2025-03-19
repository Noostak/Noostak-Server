package org.noostak.global.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.noostak.global.utils.GlobalLogger;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class ApiPathFilter extends OncePerRequestFilter {

    private static final String API_PATTERN = "/api/v1";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestUri = request.getRequestURI();
        GlobalLogger.info("[Filter] 요청 호스트 정보:",request.getRemoteHost(),request.getRemotePort());
        GlobalLogger.info("[Filter] 요청 경로 정보:",request.getMethod(),request.getRequestURI());

        if (!isApiRequest(requestUri)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private boolean isApiRequest(String requestUri) {
        return requestUri.startsWith(API_PATTERN);
    }
}