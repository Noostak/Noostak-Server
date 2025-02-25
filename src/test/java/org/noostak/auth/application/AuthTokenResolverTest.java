package org.noostak.auth.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.noostak.auth.application.jwt.JwtToken;
import org.noostak.auth.common.exception.AuthErrorCode;
import org.noostak.auth.common.exception.AuthException;
import org.noostak.auth.domain.vo.AuthId;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@DisplayName("AuthTokenResolver 테스트")
class AuthTokenResolverTest {

    private FakeAuthTokenResolver authTokenResolver;

    @BeforeEach
    void setUp() {
        authTokenResolver = new FakeAuthTokenResolver();
    }

    @Nested
    @DisplayName("토큰 저장 성공")
    class Success {

        @Test
        @DisplayName("토큰을 정상적으로 저장한다.")
        void putTokenSuccess() {
            AuthId authId = AuthId.from("test-user");
            JwtToken jwtToken = JwtToken.of("access-token", "refresh-token");

            authTokenResolver.put(authId, jwtToken);

            assertThat(authTokenResolver.isExists(authId)).isTrue();
        }

        @Test
        @DisplayName("토큰을 정상적으로 조회한다.")
        void getTokenSuccess() {
            AuthId authId = AuthId.from("test-user");
            JwtToken jwtToken = JwtToken.of("access-token", "refresh-token");

            authTokenResolver.put(authId, jwtToken);
            JwtToken foundToken = authTokenResolver.get(authId);

            assertThat(foundToken.getAccessToken()).isEqualTo("access-token");
            assertThat(foundToken.getRefreshToken()).isEqualTo("refresh-token");
        }

        @Test
        @DisplayName("토큰을 정상적으로 삭제한다.")
        void deleteTokenSuccess() {
            AuthId authId = AuthId.from("test-user");
            JwtToken jwtToken = JwtToken.of("access-token", "refresh-token");

            authTokenResolver.put(authId, jwtToken);
            authTokenResolver.delete(authId);

            assertThat(authTokenResolver.isExists(authId)).isFalse();
        }
    }

    @Nested
    @DisplayName("토큰 관리 실패")
    class Failure {

        @Test
        @DisplayName("존재하지 않는 토큰을 조회하면 예외가 발생한다.")
        void getTokenFailure() {
            AuthId authId = AuthId.from("invalid-user");

            assertThatThrownBy(() -> authTokenResolver.get(authId))
                    .isInstanceOf(AuthException.class)
                    .hasMessageContaining(AuthErrorCode.AUTH_ID_NOT_EXISTS.getMessage(authId.value()));
        }

        @Test
        @DisplayName("토큰이 만료되면 삭제된다.")
        void expiredTokenShouldBeDeleted() {
            AuthId authId = AuthId.from("expired-user");
            JwtToken jwtToken = JwtToken.of("expired-access", "expired-refresh");

            authTokenResolver.put(authId, jwtToken);
            authTokenResolver.forceExpire(authId);

            assertThatThrownBy(() -> authTokenResolver.get(authId))
                    .isInstanceOf(AuthException.class)
                    .hasMessageContaining(AuthErrorCode.AUTH_ID_NOT_EXISTS.getMessage(authId.value()));
        }
    }

    // Fake 객체 구현
    static class FakeAuthTokenResolver extends AuthTokenResolver {
        private final Map<AuthId, JwtToken> fakeStorage = new HashMap<>();

        @Override
        public void put(AuthId authId, JwtToken jwtToken) {
            fakeStorage.put(authId, jwtToken);
        }

        @Override
        public JwtToken get(AuthId authId) {
            if (!isExists(authId)) {
                throw new AuthException(AuthErrorCode.AUTH_ID_NOT_EXISTS,authId.value());
            }

            return fakeStorage.get(authId);
        }

        @Override
        public void delete(AuthId authId) {
            fakeStorage.remove(authId);
        }

        @Override
        public boolean isExists(AuthId authId) {
            return fakeStorage.containsKey(authId);
        }

        public void forceExpire(AuthId authId) {
            fakeStorage.remove(authId);
        }
    }
}
