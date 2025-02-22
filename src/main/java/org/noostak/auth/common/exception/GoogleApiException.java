package org.noostak.auth.common.exception;


public class GoogleApiException extends RuntimeException {

    public GoogleApiException(GoogleApiErrorCode errorCode) {
        super(errorCode.getMessage());
    }

    public GoogleApiException(GoogleApiErrorCode errorCode, Object ... args) {
        super(errorCode.getMessage(args));
    }
}
