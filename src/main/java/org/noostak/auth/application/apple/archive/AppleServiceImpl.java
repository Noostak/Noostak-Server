package org.noostak.auth.application.apple.archive;


import lombok.RequiredArgsConstructor;
import org.noostak.auth.application.RestClient;
import org.noostak.auth.application.apple.AppleApi;
import org.noostak.auth.application.apple.AppleService;
import org.noostak.auth.application.apple.AppleTokenRequestFactory;
import org.noostak.auth.application.apple.AppleTokenVerifier;
import org.noostak.auth.application.jwt.JwtToken;
import org.noostak.auth.application.jwt.JwtTokenProvider;
import org.noostak.auth.common.exception.ExternalApiException;
import org.noostak.auth.domain.vo.AuthId;
import org.noostak.auth.dto.apple.AppleAccessTokenRequest;
import org.noostak.auth.dto.apple.AppleAccessTokenResponse;
import org.noostak.auth.dto.apple.AppleTokenRequest;
import org.noostak.auth.dto.apple.AppleTokenResponse;
import org.springframework.stereotype.Service;

/**
 * @deprecated Use {@link org.noostak.auth.application.apple.AppleServiceImpl} instead.
 */
@Deprecated
@RequiredArgsConstructor
public class AppleServiceImpl implements AppleService {

    private final AppleTokenRequestFactory tokenRequestFactory;
    private final RestClient restClient;

    @Override
    public JwtToken requestAccessToken(String givenRefreshToken) throws ExternalApiException {
        String url = AppleApi.TOKEN_REQUEST.getUrl();

        AppleAccessTokenRequest request =
                tokenRequestFactory.createAccessTokenRequest(givenRefreshToken);

        AppleAccessTokenResponse response =
                restClient.postRequest(url,
                        request.getUrlEncodedParams(),
                        AppleAccessTokenResponse.class);

        response.validate();

        return JwtTokenProvider.createToken(response.getAccessToken(), response.getRefreshToken());
    }


    @Override
    public JwtToken requestToken(String code) {
        String url = AppleApi.TOKEN_REQUEST.getUrl();

        AppleTokenRequest request = tokenRequestFactory.createRequest(code);

        AppleTokenResponse response =
                restClient.postRequest(url,
                        request.getUrlEncodedParams(),
                        AppleTokenResponse.class);

        response.validate();

        return JwtTokenProvider.createToken(response.getIdToken(), response.getRefreshToken());
    }

    @Override
    public AuthId verify(String idToken) {
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
