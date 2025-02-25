package org.noostak.auth.application;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.noostak.auth.application.jwt.JwtToken;
import org.noostak.auth.common.exception.AuthErrorCode;
import org.noostak.auth.common.exception.AuthException;
import org.noostak.auth.domain.vo.AuthId;
import org.noostak.auth.dto.SignUpResponse;
import org.noostak.member.MemberRepositoryTest;
import org.noostak.member.domain.Member;
import org.noostak.member.domain.vo.MemberName;
import org.noostak.member.domain.vo.MemberProfileImageKey;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthInfoServiceImplTest {

    private FakeAuthInfoService authInfoService;
    private MemberRepositoryTest memberRepositoryTest;
    @BeforeEach
    void setUp() {
        authInfoService = new FakeAuthInfoService();
        memberRepositoryTest = new MemberRepositoryTest();
    }

    @Nested
    @DisplayName("회원가입 성공 케이스")
    class Success {

        @Test
        @DisplayName("새로운 AuthInfo가 저장된다.")
        void createAuthInfo_Success() {
            // given
            AuthId authId = AuthId.from("test-auth-id");
            JwtToken jwtToken = new JwtToken("access-token", "refresh-token");
            Member member = memberRepositoryTest.save(Member.of(MemberName.from("name"), MemberProfileImageKey.from("KEY")));

            // when
            SignUpResponse response = authInfoService.createAuthInfo("GOOGLE", authId, jwtToken, member);

            // then
            assertNotNull(response);
            assertEquals("access-token", response.getAccessToken());
            assertEquals("refresh-token", response.getRefreshToken());
            assertEquals(1L, response.getMemberId());
        }
    }

    @Nested
    @DisplayName("회원가입 실패 케이스")
    class Failure {

        @Test
        @DisplayName("이미 존재하는 AuthId로 가입할 수 없다.")
        void createAuthInfo_Failure_DuplicateAuthId() {
            // given
            AuthId authId = AuthId.from("test-auth-id");
            JwtToken jwtToken = new JwtToken("access-token", "refresh-token");
            Member member = memberRepositoryTest.save(Member.of(MemberName.from("name"), MemberProfileImageKey.from("KEY")));

            authInfoService.createAuthInfo("GOOGLE", authId, jwtToken, member);

            // when & then
            assertThatThrownBy(()->authInfoService.createAuthInfo("GOOGLE", authId, jwtToken, member))
                    .isInstanceOf(AuthException.class)
                    .hasMessageContaining(AuthErrorCode.AUTHID_ALREADY_EXISTS.getMessage(authId.value()));
        }
    }
}
