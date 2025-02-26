package org.noostak.infra;

import org.noostak.infra.error.S3UploadErrorCode;
import org.noostak.infra.error.S3UploadException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service("dev")
public class DevS3ServiceImpl implements S3Service {

    private DevS3ServiceImpl() {}

    public static DevS3ServiceImpl of() {
        return new DevS3ServiceImpl();
    }

    @Override
    public KeyAndUrl uploadImage(S3DirectoryPath dirPath, MultipartFile image) {
        return KeyAndUrl.of(dirPath.getName(),getImageUrl(dirPath.getPath()));
    }

    @Override
    public String getImageUrl(String key) {
        return "https://dev.aws.com/image.jpg";
    }

    @Override
    public void deleteImage(String key) {
    }
}
