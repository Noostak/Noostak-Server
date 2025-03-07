package org.noostak.auth.common.exception;

import org.noostak.global.error.core.BaseException;

public class AppleDecodeException extends BaseException {
    public AppleDecodeException(AppleDecodeErrorCode errorCode) {
        super(errorCode);
    }

    public AppleDecodeException(AppleDecodeErrorCode errorCode, Object ... args) {
        super(errorCode, args);
    }
}
