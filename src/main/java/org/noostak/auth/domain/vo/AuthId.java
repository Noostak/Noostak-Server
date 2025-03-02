package org.noostak.auth.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import org.noostak.member.common.exception.MemberErrorCode;
import org.noostak.member.common.exception.MemberException;

@Embeddable
@EqualsAndHashCode
public class AuthId {

    private final String id;

    protected AuthId() {
        this.id = null;
    }

    private AuthId(String id) {
        validate(id);
        this.id = id;
    }

    public static AuthId from(String id) {
        return new AuthId(id);
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
