package org.noostak.infra.error;

public class S3UploadException extends RuntimeException{
    public S3UploadException(S3UploadErrorCode errorCode) {
        super(errorCode.getMessage());
    }
    public S3UploadException(S3UploadErrorCode errorCode, Object ... args) {
        super(errorCode.getMessage(args));
    }
}