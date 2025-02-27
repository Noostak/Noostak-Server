package org.noostak.member.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import org.noostak.member.common.exception.MemberErrorCode;
import org.noostak.member.common.exception.MemberException;

@Embeddable
@EqualsAndHashCode
public class MemberProfileImageKey {
    private final String key;

    protected MemberProfileImageKey() {
        this.key = null;
    }

    private MemberProfileImageKey(String key) {
        if (key != null) {
            validateNotEmpty(key);
        }
        this.key = key;
    }

    public static MemberProfileImageKey from(String key) {
        if (key == null) {
            return new MemberProfileImageKey();
        }
        return new MemberProfileImageKey(key);
    }

    public String value() {
        return key;
    }

    private void validateNotEmpty(String key) {
        if (key.isBlank()) {
            throw new MemberException(MemberErrorCode.MEMBER_PROFILE_IMAGE_KEY_NOT_EMPTY);
        }
    }
}