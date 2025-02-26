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
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
    @DisplayName("멤버 생성 성공 케이스")
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
        }
    }

    @Nested
    @DisplayName("멤버 생성 실패 케이스")
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
        }
    }
}

