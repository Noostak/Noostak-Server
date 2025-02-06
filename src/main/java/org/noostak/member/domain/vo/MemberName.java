package org.noostak.member.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import org.noostak.member.common.MemberErrorCode;
import org.noostak.member.common.MemberException;

@Embeddable
@EqualsAndHashCode
public class MemberName {
    private final String name;

    protected MemberName() {
        this.name = null;
    }

    private MemberName(String name) {
        validateName(name);
        this.name = name;
    }

    public static MemberName from(String name) {
        return new MemberName(name);
    }

    public String value() {
        return name;
    }

    private void validateName(String name) {
        validateEmpty(name);
        validateLength(name);
        validateSpecialCharacters(name);
    }

    private void validateEmpty(String memberName) {
        if (memberName == null || memberName.isBlank()) {
            throw new MemberException(MemberErrorCode.MEMBER_NAME_NOT_EMPTY);
        }
    }

    private void validateLength(String memberName) {
        if (memberName.length() > 15) {
            throw new MemberException(MemberErrorCode.MEMBER_NAME_LENGTH_EXCEEDED);
        }
    }

    private void validateSpecialCharacters(String name) {
        if (name.matches(".*[!@#$%^&*()_+=|<>?{}\\[\\]~-].*")) {
            throw new MemberException(MemberErrorCode.MEMBER_NAME_INVALID_CHARACTER);
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
