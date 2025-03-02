package org.noostak.infra;

import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;

public interface S3Storage {
    KeyAndUrl upload(S3DirectoryPath fileName, MultipartFile image) throws IOException;

    void delete(String key);

    String findPublicUrlByKey(String key);

    S3Client getS3Client();

    String getS3BucketName();

    long getMaxFileSize();
}