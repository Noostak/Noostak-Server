package org.noostak.auth.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.noostak.auth.application.jwt.JwtToken;
import org.noostak.auth.application.jwt.JwtTokenProvider;
import org.noostak.auth.common.exception.KakaoApiErrorCode;
import org.noostak.auth.domain.vo.AuthId;
import org.noostak.auth.dto.*;
import org.noostak.global.KakaoTokenRequestFactory;
import org.springframework.http.HttpHeaders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KakaoServiceImplTest {

    @Mock
    private KakaoTokenRequestFactory kakaoTokenRequestFactory;

    @Mock
    private RestClient restClient;

    @InjectMocks
    private KakaoServiceImpl kakaoService;

    private final String MOCK_CODE = "testCode";
    private final String MOCK_ACCESS_TOKEN = "testAccessToken";
    private final String MOCK_REFRESH_TOKEN = "testRefreshToken";
    private final String MOCK_KAKAO_ID = "12345";

    private final String ERROR_MSG = KakaoApiErrorCode.KAKAO_API_ERROR.getMessage();

    @BeforeEach
    void setUp() {
        // 테스트 준비
    }

    @Nested
    @DisplayName("성공 케이스")
    class Success {
        @Test
        @DisplayName("토큰 정보 조회 테스트")
        void fetchTokenInfoTest() {
            // given
            KakaoTokenInfoResponse mockResponse = mock(KakaoTokenInfoResponse.class);
            when(mockResponse.getId()).thenReturn(MOCK_KAKAO_ID);
            doNothing().when(mockResponse).validate();

            when(restClient.postRequest(
                    eq(KaKaoApi.FETCH_TOKEN.getUrl()),
                    any(HttpHeaders.class),
                    eq(KakaoTokenInfoResponse.class)))
                    .thenReturn(mockResponse);

            try (MockedStatic<TokenInfo> mockedStatic = mockStatic(TokenInfo.class)) {
                TokenInfo expectedTokenInfo = mock(TokenInfo.class);
                mockedStatic.when(() -> TokenInfo.of(MOCK_KAKAO_ID)).thenReturn(expectedTokenInfo);

                // when
                TokenInfo result = kakaoService.fetchTokenInfo(MOCK_ACCESS_TOKEN);

                // then
                assertThat(result).isEqualTo(expectedTokenInfo);

                verify(mockResponse).validate();
                verify(restClient).postRequest(
                        eq(KaKaoApi.FETCH_TOKEN.getUrl()),
                        any(HttpHeaders.class),
                        eq(KakaoTokenInfoResponse.class));
            }
        }

        @Test
        @DisplayName("토큰 요청 테스트")
        void requestTokenTest() {
            // given
            KakaoTokenRequest mockRequest = mock(KakaoTokenRequest.class);
            KakaoTokenResponse mockResponse = mock(KakaoTokenResponse.class);

            when(mockRequest.getUrlEncodedParams()).thenReturn("code=testCode");
            when(mockResponse.getAccessToken()).thenReturn(MOCK_ACCESS_TOKEN);
            when(mockResponse.getRefreshToken()).thenReturn(MOCK_REFRESH_TOKEN);
            doNothing().when(mockResponse).validate();

            when(kakaoTokenRequestFactory.createRequest(MOCK_CODE)).thenReturn(mockRequest);
            when(restClient.postRequest(
                    eq(KaKaoApi.TOKEN_REQUEST.getUrl()),
                    anyString(),
                    eq(KakaoTokenResponse.class)))
                    .thenReturn(mockResponse);

            JwtToken expectedToken = new JwtToken("Bearer", MOCK_ACCESS_TOKEN, MOCK_REFRESH_TOKEN);

            try (MockedStatic<JwtTokenProvider> mockedStatic = mockStatic(JwtTokenProvider.class)) {
                mockedStatic.when(() -> JwtTokenProvider.createToken(MOCK_ACCESS_TOKEN, MOCK_REFRESH_TOKEN))
                        .thenReturn(expectedToken);

                // when
                JwtToken result = kakaoService.requestToken(MOCK_CODE);

                // then
                assertThat(result).isEqualTo(expectedToken);

                verify(mockResponse).validate();
                verify(kakaoTokenRequestFactory).createRequest(MOCK_CODE);
                verify(restClient).postRequest(
                        eq(KaKaoApi.TOKEN_REQUEST.getUrl()),
                        anyString(),
                        eq(KakaoTokenResponse.class));
            }
        }

        @Test
        @DisplayName("로그인 테스트")
        void loginTest() {
            // given
            KakaoMyInfoResponse mockResponse = mock(KakaoMyInfoResponse.class);
            when(mockResponse.getId()).thenReturn(MOCK_KAKAO_ID);
            doNothing().when(mockResponse).validate();

            AuthId expectedAuthId = AuthId.from(MOCK_KAKAO_ID);

            when(restClient.postRequest(
                    eq(KaKaoApi.FETCH_MY_INFO.getUrl()),
                    any(HttpHeaders.class),
                    eq(KakaoMyInfoResponse.class)))
                    .thenReturn(mockResponse);

            try (MockedStatic<AuthId> mockedStatic = mockStatic(AuthId.class)) {
                mockedStatic.when(() -> AuthId.from(MOCK_KAKAO_ID)).thenReturn(expectedAuthId);

                // when
                AuthId result = kakaoService.login(MOCK_ACCESS_TOKEN);

                // then
                assertThat(result).isEqualTo(expectedAuthId);

                verify(mockResponse).validate();
                verify(restClient).postRequest(
                        eq(KaKaoApi.FETCH_MY_INFO.getUrl()),
                        any(HttpHeaders.class),
                        eq(KakaoMyInfoResponse.class));
            }
        }

        @Test
        @DisplayName("인증 헤더 생성 테스트")
        void makeAuthorizationBearerTokenHeaderTest() {
            // given
            String token = "testToken";

            // when
            HttpHeaders headers = kakaoService.makeAuthorizationBearerTokenHeader(token);

            // then
            assertThat(headers).isNotNull();
            assertThat(headers.getFirst("Authorization")).isEqualTo("Bearer " + token);
        }
    }
}