package org.noostak.likes.common;

public class LikesException extends RuntimeException {
    public LikesException(LikesErrorCode errorCode) {
        super(errorCode.getMessage());
    }
}
