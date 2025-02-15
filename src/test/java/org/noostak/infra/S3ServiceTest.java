package org.noostak.infra;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.noostak.infra.error.S3UploadErrorCode;
import org.noostak.infra.error.S3UploadException;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

class S3ServiceTest {

    private final S3Client s3Client = mock(S3Client.class);
    private final String bucketName = "test-bucket";
    private final long maxFileSize = 2L * 1024 * 1024; // 2MB 제한

    private final S3Storage s3Storage =
            FakeS3StorageImpl.of(s3Client,
                    bucketName,
                    maxFileSize);

    private final S3ServiceImpl s3ServiceImpl = S3ServiceImpl.of(s3Storage);

    @Nested
    @DisplayName("이미지 업로드 테스트")
    class UploadImageTests {

        @Test
        @DisplayName("이미지 업로드 - 성공")
        void uploadImage_success() throws IOException {
            // Given
            MultipartFile image = new MockMultipartFile(
                    "image",
                    "test.jpg",
                    "image/jpeg",
                    new byte[]{1, 2, 3, 4}
            );

            // When
            S3DirectoryPath directoryPath = S3DirectoryPath.DEFAULT ;
            KeyAndUrl result = s3ServiceImpl.uploadImage(directoryPath, image);

            // Then
            assertThat(result.getKey()).startsWith(directoryPath.getPath()).endsWith(".jpg");
            verify(s3Storage.getS3Client()).putObject(any(PutObjectRequest.class), any(RequestBody.class));
        }


        @Test
        @DisplayName("이미지 업로드 - 잘못된 확장자")
        void uploadImage_invalidExtension() {
            // Given
            String directoryPath = "images/";
            MultipartFile image = new MockMultipartFile(
                    "image",
                    "test.txt",
                    "text/plain",
                    new byte[]{1, 2, 3, 4}
            );

            // When & Then
            assertThatThrownBy(() -> s3ServiceImpl.uploadImage(S3DirectoryPath.DEFAULT, image))
                    .isInstanceOf(S3UploadException.class)
                    .hasMessageContaining(S3UploadErrorCode.INVALID_EXTENSION.getMessage());
        }

        @Test
        @DisplayName("이미지 업로드 - 파일 크기 초과")
        void uploadImage_fileSizeExceedsLimit() {
            // Given
            String directoryPath = "images/";
            MultipartFile image = new MockMultipartFile(
                    "image",
                    "large.jpg",
                    "image/jpeg",
                    new byte[3 * 1024 * 1024]
            );

            // When & Then
            assertThatThrownBy(() -> s3ServiceImpl.uploadImage(S3DirectoryPath.DEFAULT, image))
                    .isInstanceOf(S3UploadException.class)
                    .hasMessageContaining(S3UploadErrorCode.FILE_SIZE_EXCEEDED.getMessage());
        }
    }

    @Nested
    @DisplayName("이미지 삭제 테스트")
    class DeleteImageTests {

        @Test
        @DisplayName("이미지 삭제 - 성공")
        void deleteImage_success() {
            // Given
            String key = "images/test.jpg";

            // When
            s3ServiceImpl.deleteImage(key);

            // Then
            verify(s3Storage.getS3Client()).deleteObject(any(DeleteObjectRequest.class));
        }
    }
}
