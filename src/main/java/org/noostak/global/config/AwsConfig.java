package org.noostak.global.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.noostak.infra.S3Storage;
import org.noostak.infra.S3StorageImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@Getter
@RequiredArgsConstructor
public class AwsConfig {

    @Value("${aws-property.s3-bucket-name}")
    private String s3BucketName;

    @Value("${aws-property.access-key}")
    private String accessKey;

    @Value("${aws-property.secret-key}")
    private String secretKey;

    @Value("${aws-property.aws-region}")
    private String awsRegion;

    @Value("${aws-property.max-file-size}")
    private String maxFileSize;


    @Bean
    public S3Storage s3Storage(){
        return S3StorageImpl.of(S3Client(), S3BucketName(),maxFileSize());
    }

    @Bean
    public Region region() {
        return Region.of(awsRegion);
    }

    @Bean
    public S3Client S3Client() {
        return S3Client.builder()
                .region(region())
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)
                ))
                .build();
    }

    @Bean
    public Long maxFileSize() {
        try {
            return Long.parseLong(maxFileSize);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid max file size format: " + maxFileSize, e);
        }
    }

    @Bean
    public String S3BucketName(){
        return this.s3BucketName;
    }
}