package org.noostak.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.noostak.auth.dto.SignUpRequest;
import org.noostak.infra.KeyAndUrl;
import org.noostak.infra.S3DirectoryPath;
import org.noostak.infra.S3Service;
import org.noostak.member.application.MemberServiceImpl;
import org.noostak.member.common.exception.MemberException;
import org.noostak.member.domain.vo.MemberName;
import org.noostak.member.domain.vo.MemberProfileImageKey;
import org.noostak.member.dto.GetProfileResponse;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private S3Service s3Service;

    @InjectMocks
    private MemberServiceImpl memberService;

    @Nested
    @DisplayName("멤버 성공 케이스")
    class Success {
        @Test
        @DisplayName("정상적인 회원 가입 요청 시 멤버가 성공적으로 생성된다")
        void createMember_Success() {
            // given
            SignUpRequest request = mock(SignUpRequest.class);
            MultipartFile mockFile = mock(MultipartFile.class);
            when(request.getMemberProfileImage()).thenReturn(mockFile);
            when(request.getMemberName()).thenReturn("홍길동");
            when(s3Service.uploadImage(eq(S3DirectoryPath.MEMBER), any(MultipartFile.class)))
                    .thenReturn(KeyAndUrl.of("profile-key", "profile-url"));
            when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // when
            Member createdMember = memberService.createMember(request);

            // then
            assertNotNull(createdMember);
            assertEquals("홍길동", createdMember.getName().value());
            assertEquals("profile-key", createdMember.getKey().value());

            verify(s3Service, times(1)).uploadImage(eq(S3DirectoryPath.MEMBER), eq(mockFile));
            verify(memberRepository, times(1)).save(any(Member.class));
        }

        @Test
        @DisplayName("회원 프로필 조회가 성공적으로 이루어져야 한다")
        void fetchMember_Success() {
            // given
            Long memberId = 1L;
            Member member = Member.of(
                    MemberName.from("홍길동"),
                    MemberProfileImageKey.from("profile-key")
            );

            when(memberRepository.getById(memberId)).thenReturn(member);
            when(s3Service.getImageUrl("profile-key")).thenReturn("profile-url");

            // when
            GetProfileResponse response = memberService.fetchMember(memberId);

            // then
            assertNotNull(response);
            assertEquals("홍길동", response.getMembername());
            assertEquals("profile-url", response.getMemberProfileImage());

            verify(memberRepository, times(1)).getById(memberId);
            verify(s3Service, times(1)).getImageUrl("profile-key");
        }
    }

    @Nested
    @DisplayName("멤버 실패 케이스")
    class Failure {
        @Test
        @DisplayName("프로필 이미지 업로드 실패 시 예외 발생")
        void createMember_Fail_WhenProfileUploadFails() {
            // given
            SignUpRequest request = mock(SignUpRequest.class);
            MultipartFile mockFile = mock(MultipartFile.class);
            when(request.getMemberProfileImage()).thenReturn(mockFile);
            when(s3Service.uploadImage(eq(S3DirectoryPath.MEMBER), any(MultipartFile.class)))
                    .thenThrow(new RuntimeException("S3 업로드 실패"));

            // when & then
            assertThrows(RuntimeException.class, () -> memberService.createMember(request));

            verify(s3Service, times(1)).uploadImage(eq(S3DirectoryPath.MEMBER), eq(mockFile));
            verify(memberRepository, never()).save(any(Member.class));
        }

        @Test
        @DisplayName("존재하지 않는 회원 ID로 조회시 예외가 발생해야 한다")
        void fetchMember_Fail_WithNonExistingId() {
            // given
            Long nonExistingMemberId = 999L;
            when(memberRepository.getById(nonExistingMemberId))
                    .thenThrow(new IllegalArgumentException("회원을 찾을 수 없습니다"));

            // when & then
            assertThrows(IllegalArgumentException.class, () -> {
                memberService.fetchMember(nonExistingMemberId);
            });

            verify(memberRepository, times(1)).getById(nonExistingMemberId);
            verify(s3Service, never()).getImageUrl(anyString());
        }

        @Test
        @DisplayName("잘못된 회원명으로 생성 시도시 예외가 발생해야 한다")
        void createMember_Fail_WithInvalidName() {
            // given
            SignUpRequest request = mock(SignUpRequest.class);
            MultipartFile mockFile = mock(MultipartFile.class);
            when(request.getMemberProfileImage()).thenReturn(mockFile);
            when(request.getMemberName()).thenReturn("");  // 빈 문자열은 유효하지 않다고 가정

            KeyAndUrl keyAndUrl = KeyAndUrl.of("profile-key", "profile-url");
            when(s3Service.uploadImage(any(S3DirectoryPath.class), any(MultipartFile.class)))
                    .thenReturn(keyAndUrl);

            // when & then
            assertThrows(MemberException.class, () -> {
                memberService.createMember(request);
            });

            verify(s3Service, times(1)).uploadImage(any(S3DirectoryPath.class), eq(mockFile));
            verify(memberRepository, never()).save(any(Member.class));
        }
    }
}