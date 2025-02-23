package org.noostak.auth.common.exception;


import org.noostak.global.error.core.BaseException;
import org.noostak.global.error.core.ErrorCode;

public class ExternalApiException extends BaseException {

    public ExternalApiException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ExternalApiException(ErrorCode errorCode, Object ... args) {
        super(errorCode,args);
    }
}
