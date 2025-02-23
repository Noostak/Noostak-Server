package org.noostak.auth.common.exception;


public class KakaoApiException extends ExternalApiException {

    public KakaoApiException(KakaoApiErrorCode errorCode) {
        super(errorCode);
    }

    public KakaoApiException(KakaoApiErrorCode errorCode, Object ... args) {
        super(errorCode, args);
    }
}
