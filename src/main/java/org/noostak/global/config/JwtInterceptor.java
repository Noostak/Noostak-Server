package org.noostak.global.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.noostak.auth.application.AuthInfoService;
import org.noostak.auth.common.exception.AuthErrorCode;
import org.noostak.auth.common.exception.AuthException;
import org.noostak.auth.domain.AuthInfo;
import org.noostak.global.utils.GlobalLogger;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {
    private final AuthInfoService authInfoService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String accessToken = extractToken(request);
        GlobalLogger.info("[Intercept] 요청 호스트 정보:",request.getRemoteHost(),request.getRemotePort());
        GlobalLogger.info("[Intercept] 요청 경로 정보:",request.getMethod(),request.getRequestURI());

        AuthInfo authInfo = authInfoService.verify(accessToken);
        Long memberId = authInfo.getMember().getId();

        request.setAttribute("memberId", memberId);

        GlobalLogger.info("[Intercept] authId: ",authInfo.getAuthId().value());
        GlobalLogger.info("[Intercept] memberId: ",memberId);

        return true;
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        throw new AuthException(AuthErrorCode.INVALID_TOKEN);
    }
}
