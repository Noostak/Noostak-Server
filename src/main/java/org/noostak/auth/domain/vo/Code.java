package org.noostak.auth.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import org.noostak.member.common.MemberErrorCode;
import org.noostak.member.common.MemberException;

@Embeddable
@EqualsAndHashCode
public class Code {

    private final String id;

    protected Code() {
        this.id = null;
    }

    private Code(String id) {
        validate(id);
        this.id = id;
    }

    public static Code from(String id) {
        return new Code(id);
    }

    public String value() {
        return id;
    }

    private void validate(String id) {
        validateNotNull(id);
        validateEmpty(id);
    }

    private void validateEmpty(String id) {
        if (id.isBlank()) {
            throw new MemberException(MemberErrorCode.AUTH_ID_NOT_EMPTY);
        }
    }

    private void validateNotNull(String id) {
        if (id == null) {
            throw new MemberException(MemberErrorCode.AUTH_ID_NOT_NULL);
        }
    }
}
