package org.noostak.auth.common.exception;


public class AuthException extends RuntimeException {

    public AuthException(AuthErrorCode errorCode) {
        super(errorCode.getMessage());
    }

    public AuthException(AuthErrorCode errorCode, Object ... args) {
        super(errorCode.getMessage(args));
    }
}
