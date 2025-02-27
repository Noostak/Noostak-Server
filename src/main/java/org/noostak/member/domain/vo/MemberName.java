package org.noostak.member.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import org.noostak.member.common.exception.MemberErrorCode;
import org.noostak.member.common.exception.MemberException;

import java.util.regex.Pattern;

@Embeddable
@EqualsAndHashCode
public class MemberName {
    private static final int MAX_LENGTH = 15;
    private static final Pattern INVALID_PATTERN = Pattern.compile("[^\uAC00-\uD7A3a-zA-Z\uD83C-\uDBFF\uDC00-\uDFFF\u200D\uFE0F]");

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
        validateInvalidCharacters(name);
    }

    private void validateEmpty(String name) {
        if (name == null || name.isBlank()) {
            throw new MemberException(MemberErrorCode.MEMBER_NAME_NOT_EMPTY);
        }
    }

    private void validateLength(String name) {
        if (name.length() > MAX_LENGTH) {
            throw new MemberException(MemberErrorCode.MEMBER_NAME_LENGTH_EXCEEDED);
        }
    }

    private void validateInvalidCharacters(String name) {
        if (INVALID_PATTERN.matcher(name).find()) {
            throw new MemberException(MemberErrorCode.INVALID_MEMBER_NAME);
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
