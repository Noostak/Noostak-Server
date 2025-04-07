package org.noostak.global.error.core;

public class TimeEntityException extends BaseException{
    public TimeEntityException(ErrorCode errorCode) {
        super(errorCode);
    }

    public TimeEntityException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
