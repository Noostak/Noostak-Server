package org.noostak.group.common.exception;

public class GroupException extends RuntimeException {
    public GroupException(GroupErrorCode errorCode) {
        super(errorCode.getMessage());
    }
}
