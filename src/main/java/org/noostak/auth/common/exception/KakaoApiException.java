package org.noostak.auth.common.exception;


public class KakaoApiException extends RuntimeException {

    public KakaoApiException(KakaoApiErrorCode errorCode) {
        super(errorCode.getMessage());
    }

    public KakaoApiException(KakaoApiErrorCode errorCode, Object ... args) {
        super(errorCode.getMessage(args));
    }
}
