package org.noostak.group.common.exception;

import org.noostak.global.error.core.BaseException;

public class GroupException extends BaseException {
    public GroupException(GroupErrorCode errorCode) {
        super(errorCode);
    }
}
