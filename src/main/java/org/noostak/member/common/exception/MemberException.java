package org.noostak.member.common.exception;

public class MemberException extends RuntimeException {
    public MemberException(MemberErrorCode errorCode) {
        super(errorCode.getMessage());
    }
}
