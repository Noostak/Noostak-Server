package org.noostak.auth.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import org.noostak.member.common.exception.MemberErrorCode;
import org.noostak.member.common.exception.MemberException;

@Embeddable
@EqualsAndHashCode
public class Code {

    private final String code;

    protected Code() {
        this.code = null;
    }

    private Code(String code) {
        validate(code);
        this.code = code;
    }

    public static Code from(String code) {
        return new Code(code);
    }

    public String value() {
        return code;
    }

    private void validate(String id) {
        validateNotNull(id);
        validateEmpty(id);
    }

    private void validateEmpty(String code) {
        if (code.isBlank()) {
            throw new MemberException(MemberErrorCode.AUTH_ID_NOT_EMPTY);
        }
    }

    private void validateNotNull(String code) {
        if (code == null) {
            throw new MemberException(MemberErrorCode.AUTH_ID_NOT_NULL);
        }
    }
}
