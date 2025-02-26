package org.noostak.infra.error;

public class S3DeleteException extends RuntimeException{
    public S3DeleteException(S3DeleteErrorCode errorCode) {
        super(errorCode.getMessage());
    }
    public S3DeleteException(S3DeleteErrorCode errorCode, Object ... args) {
        super(errorCode.getMessage(args));
    }
}