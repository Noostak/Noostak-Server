package org.noostak.likes.common;

public class LikesException extends RuntimeException {
    public LikesException(LikesErrorCode errorCode) {
        super(errorCode.getMessage());
    }

    public LikesException(LikesErrorCode errorCode, Object ... args) {
        super(errorCode.getMessage(args));
    }
}
