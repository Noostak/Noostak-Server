package org.noostak.member.common;

public class MemberException extends RuntimeException {
    public MemberException(MemberErrorCode errorCode) {
        super(errorCode.getMessage());
    }
}
