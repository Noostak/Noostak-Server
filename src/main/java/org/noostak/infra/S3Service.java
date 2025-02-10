package org.noostak.infra;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class S3Service {

    private final S3Storage s3Storage;

    private S3Service(S3Storage s3Storage) {
        this.s3Storage = s3Storage;
    }

    public static S3Service of(S3Storage s3Storage) {
        return new S3Service(s3Storage);
    }

    public String uploadImage(String directoryPath, MultipartFile image) throws IOException {
        return s3Storage.upload(directoryPath, image);
    }

    public void deleteImage(String key) {
        s3Storage.delete(key);
    }
}
