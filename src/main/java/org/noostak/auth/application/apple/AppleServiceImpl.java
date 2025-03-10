package org.noostak.auth.application.apple;


import lombok.RequiredArgsConstructor;
import org.noostak.auth.application.RestClient;
import org.noostak.auth.application.jwt.JwtToken;
import org.noostak.auth.application.jwt.JwtTokenProvider;
import org.noostak.auth.common.exception.ExternalApiException;
import org.noostak.auth.domain.vo.AuthId;
import org.noostak.auth.dto.apple.AppleAccessTokenRequest;
import org.noostak.auth.dto.apple.AppleAccessTokenResponse;
import org.noostak.auth.dto.apple.AppleTokenRequest;
import org.noostak.auth.dto.apple.AppleTokenResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppleServiceImpl implements AppleService {

    private final RestClient restClient;

    @Override
    public AuthId verifyByAuthAccessToken(String idToken) {
        String authId = AppleTokenVerifier.verifyIdToken(idToken);
        return AuthId.from(authId);
    }

    @Override
    public void logout(String accessToken) {
        // 별도로 로직 처리하지 않음
    }

    @Override
    public void unlink(String accessToken) {
        // 별도로 로직 처리하지 않음
    }
}
