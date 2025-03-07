package org.noostak.auth.common.exception;


public class AppleApiException extends ExternalApiException {

    public AppleApiException(AppleApiErrorCode errorCode) {
        super(errorCode);
    }

    public AppleApiException(AppleApiErrorCode errorCode, Object ... args) {
        super(errorCode, args);
    }
}
