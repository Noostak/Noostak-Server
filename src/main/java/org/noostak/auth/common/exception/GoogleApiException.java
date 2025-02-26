package org.noostak.auth.common.exception;


public class GoogleApiException extends ExternalApiException {

    public GoogleApiException(GoogleApiErrorCode errorCode) {
        super(errorCode);
    }

    public GoogleApiException(GoogleApiErrorCode errorCode, Object ... args) {
        super(errorCode, args);
    }
}
