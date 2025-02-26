package org.noostak.infra;

import org.noostak.infra.error.S3DeleteErrorCode;
import org.noostak.infra.error.S3DeleteException;
import org.noostak.infra.error.S3UploadErrorCode;
import org.noostak.infra.error.S3UploadException;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Primary
@Service("prod")
public class S3ServiceImpl implements S3Service {

    private final S3Storage s3Storage;

    private S3ServiceImpl(S3Storage s3Storage) {
        this.s3Storage = s3Storage;
    }

    public static S3ServiceImpl of(S3Storage s3Storage) {
        return new S3ServiceImpl(s3Storage);
    }

    @Override
    public KeyAndUrl uploadImage(S3DirectoryPath dirPath, MultipartFile image) {
        try {
            return s3Storage.upload(dirPath, image);
        } catch (Exception e) {
            throw new S3UploadException(S3UploadErrorCode.IMAGE_UPLOAD_FAILED,e.getMessage());
        }
    }

    @Override
    public String getImageUrl(String key) {
        return s3Storage.findPublicUrlByKey(key);
    }

    @Override
    public void deleteImage(String key) {
        try {
            s3Storage.delete(key);
        } catch (Exception e) {
            throw new S3DeleteException(S3DeleteErrorCode.IMAGE_DELETE_FAILED,e.getMessage());
        }
    }
}
