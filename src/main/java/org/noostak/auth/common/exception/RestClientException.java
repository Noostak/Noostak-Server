package org.noostak.auth.common.exception;

import org.noostak.global.error.core.BaseException;

public class RestClientException extends BaseException {
    public RestClientException(RestClientErrorCode errorCode) {
        super(errorCode);
    }

    public RestClientException(RestClientErrorCode errorCode, Object ... args) {
        super(errorCode,args);
    }
}