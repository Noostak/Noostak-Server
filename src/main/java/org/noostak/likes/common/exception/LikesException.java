package org.noostak.likes.common.exception;

import org.noostak.global.error.core.BaseException;

public class LikesException extends BaseException {
    public LikesException(LikesErrorCode errorCode) {
        super(errorCode);
    }

    public LikesException(LikesErrorCode errorCode, Object ... args) {
        super(errorCode, args);
    }
}
