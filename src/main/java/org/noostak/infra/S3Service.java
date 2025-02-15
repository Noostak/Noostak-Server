package org.noostak.infra;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface S3Service {
    KeyAndUrl uploadImage(S3DirectoryPath dirPath, MultipartFile image) throws IOException;
    String getImageUrl(String key);
    void deleteImage(String key);
}
