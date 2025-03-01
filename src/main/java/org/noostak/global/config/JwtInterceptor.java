package org.noostak.global.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.noostak.auth.application.AuthInfoService;
import org.noostak.auth.application.OauthService;
import org.noostak.auth.application.OauthServiceFactory;
import org.noostak.auth.application.jwt.JwtToken;
import org.noostak.auth.application.jwt.JwtTokenProvider;
import org.noostak.auth.common.exception.AuthErrorCode;
import org.noostak.auth.common.exception.AuthException;
import org.noostak.auth.common.exception.ExternalApiException;
import org.noostak.auth.common.exception.RestClientException;
import org.noostak.auth.common.success.AuthSuccessCode;
import org.noostak.auth.domain.AuthInfo;
import org.noostak.auth.domain.vo.AuthId;
import org.noostak.auth.domain.vo.AuthType;
import org.noostak.auth.dto.TokenResponse;
import org.noostak.global.success.SuccessResponse;
import org.noostak.global.utils.GlobalLogger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {
    private final OauthServiceFactory oauthServiceFactory;
    private final AuthInfoService authInfoService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = extractToken(request);

        if (token != null) {

            // TODO: 외부 API 호출 횟수를 줄이는 방법 탐구 (ex. 액세스 토큰 캐싱)
            for(AuthType authType : AuthType.values()){
                OauthService oauthService = oauthServiceFactory.getService(authType);

                try {
                    AuthId authId = oauthService.verify(token);
                    Long memberId = authInfoService.findByAuthId(authId).getMember().getId();

                    request.setAttribute("memberId", memberId);
                    return true;
                }catch (ExternalApiException | RestClientException e){
                    GlobalLogger.warn(AuthErrorCode.INVALID_TOKEN.getMessage());
                }
            }
        }

        throw new AuthException(AuthErrorCode.INVALID_TOKEN);
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
