package org.noostak.member.common.exception;

import org.noostak.global.error.core.BaseException;

public class MemberException extends BaseException {
    public MemberException(MemberErrorCode errorCode) {
        super(errorCode);
    }
}
