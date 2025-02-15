package org.noostak.infra;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class S3ServiceImpl implements S3Service {

    private final S3Storage s3Storage;

    private S3ServiceImpl(S3Storage s3Storage) {
        this.s3Storage = s3Storage;
    }

    public static S3ServiceImpl of(S3Storage s3Storage) {
        return new S3ServiceImpl(s3Storage);
    }

    @Override
    public KeyAndUrl uploadImage(S3DirectoryPath dirPath, MultipartFile image) throws IOException {
        return s3Storage.upload(dirPath, image);
    }

    @Override
    public String findImageUrl(String key) {
        return s3Storage.findPublicUrlByKey(key);
    }

    @Override
    public void deleteImage(String key) {
        s3Storage.delete(key);
    }
}