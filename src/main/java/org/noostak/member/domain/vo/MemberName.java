package org.noostak.member.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import org.noostak.member.common.MemberErrorCode;
import org.noostak.member.common.MemberException;

@Embeddable
@EqualsAndHashCode
public class MemberName {
    private static final int MAX_LENGTH = 15;

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
        validateNumbers(name);
        validateSpecialCharacters(name);
        validateInvalidLanguages(name);
    }

    private void validateEmpty(String memberName) {
        if (memberName == null || memberName.isBlank()) {
            throw new MemberException(MemberErrorCode.MEMBER_NAME_NOT_EMPTY);
        }
    }

    private void validateLength(String memberName) {
        if (memberName.length() > MAX_LENGTH) {
            throw new MemberException(MemberErrorCode.MEMBER_NAME_LENGTH_EXCEEDED);
        }
    }

    private void validateNumbers(String name) {
        if (name.chars().anyMatch(Character::isDigit)) {
            throw new MemberException(MemberErrorCode.MEMBER_NAME_CANNOT_CONTAIN_NUMBERS);
        }
    }

    private void validateSpecialCharacters(String name) {
        if (name.chars().mapToObj(c -> (char) c).anyMatch(this::isSpecialCharacter)) {
            throw new MemberException(MemberErrorCode.MEMBER_NAME_CANNOT_CONTAIN_SPECIAL_CHARACTERS);
        }
    }

    private void validateInvalidLanguages(String name) {
        if (name.chars().mapToObj(c -> (char) c).anyMatch(this::isInvalidLanguage)) {
            throw new MemberException(MemberErrorCode.MEMBER_NAME_INVALID_LANGUAGE);
        }
    }

    private boolean isKorean(char c) {
        return (c >= '가' && c <= '힣');
    }

    private boolean isEnglish(char c) {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
    }

    private boolean isSpecialCharacter(char c) {
        return !Character.isLetterOrDigit(c);
    }

    private boolean isInvalidLanguage(char c) {
        return !isKorean(c) && !isEnglish(c);
    }

    @Override
    public String toString() {
        return name;
    }
}
