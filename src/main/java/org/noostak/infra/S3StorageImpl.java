package org.noostak.infra;

import org.noostak.infra.error.S3UploadErrorCode;
import org.noostak.infra.error.S3UploadException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


@Component
public class S3StorageImpl implements S3Storage {

    private static final List<String> IMAGE_EXTENSIONS = Arrays.asList("image/jpeg", "image/png", "image/jpg", "image/webp");

    private final S3Client s3Client;
    private final String s3BucketName;
    private final long maxFileSize;

    private S3StorageImpl(S3Client s3Client, String s3BucketName, long maxFileSize) {
        this.s3Client = s3Client;
        this.s3BucketName = s3BucketName;
        this.maxFileSize = maxFileSize;
    }


    public static S3StorageImpl of(S3Client s3Client, String s3BucketName, long maxFileSize) {
        return new S3StorageImpl(s3Client, s3BucketName, maxFileSize);
    }

    @Override
    public KeyAndUrl upload(S3DirectoryPath dirPath, MultipartFile image) throws IOException {
        final String fileName = dirPath.getPath() + generateImageFileName();

        validateImage(image);

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(s3BucketName)
                .key(fileName)
                .contentType(image.getContentType())
                .contentDisposition("inline")
                .build();

        RequestBody requestBody = RequestBody.fromBytes(image.getBytes());

        s3Client.putObject(request, requestBody);

        String publicUrl = findPublicUrlByKey(fileName);

        return KeyAndUrl.of(fileName, publicUrl);
    }

    private void validateImage(MultipartFile image) {
        validateExtension(image);
        validateFileSize(image);
    }

    @Override
    public void delete(String key) {
        DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                .bucket(s3BucketName)
                .key(key)
                .build();

        s3Client.deleteObject(deleteRequest);
    }

    @Override
    public String findPublicUrlByKey(String key) {
        GetUrlRequest urlRequest =
                GetUrlRequest.builder().bucket(s3BucketName).key(key).build();

        return s3Client.utilities().getUrl(urlRequest).toString();
    }


    private void validateExtension(MultipartFile image) {
        String contentType = image.getContentType();
        if (!IMAGE_EXTENSIONS.contains(contentType)) {
            throw new S3UploadException(S3UploadErrorCode.INVALID_EXTENSION);
        }
    }

    private void validateFileSize(MultipartFile image) {
        if (image.getSize() > maxFileSize) {
            throw new S3UploadException(S3UploadErrorCode.FILE_SIZE_EXCEEDED);
        }
    }

    private String generateImageFileName() {
        return UUID.randomUUID() + ".jpg";
    }

    @Override
    public S3Client getS3Client() {
        return s3Client;
    }

    @Override
    public String getS3BucketName() {
        return s3BucketName;
    }

    @Override
    public long getMaxFileSize() {
        return maxFileSize;
    }
}