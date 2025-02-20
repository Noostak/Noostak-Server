package org.noostak.auth.common.exception;


import org.noostak.global.error.core.BaseException;

public class AuthException extends BaseException {

    public AuthException(AuthErrorCode errorCode) {
        super(errorCode);
    }

    public AuthException(AuthErrorCode errorCode, Object ... args) {
        super(errorCode,args);
    }
}
