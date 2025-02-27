package org.noostak.infra;

import org.springframework.web.multipart.MultipartFile;

public interface S3Service {
    KeyAndUrl uploadImage(S3DirectoryPath dirPath, MultipartFile image);
    String getImageUrl(String key);
    void deleteImage(String key);
}
