package org.noostak.infra;

import org.noostak.global.config.AwsConfig;
import org.noostak.infra.error.S3UploadErrorCode;
import org.noostak.infra.error.S3UploadException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
public class S3Service {

    private static final List<String> IMAGE_EXTENSIONS = Arrays.asList("image/jpeg", "image/png", "image/jpg", "image/webp");

    private final AwsConfig awsConfig;

    public S3Service(AwsConfig awsConfig) {
        this.awsConfig = awsConfig;
    }

}
