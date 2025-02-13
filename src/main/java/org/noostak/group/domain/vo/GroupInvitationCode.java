package org.noostak.group.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import org.noostak.group.common.exception.GroupErrorCode;
import org.noostak.group.common.exception.GroupException;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Embeddable
@EqualsAndHashCode
public class GroupInvitationCode {

    private static final int CODE_LENGTH = 6;
    private static final Set<Character> ALLOWED_CHARACTERS = createAllowedCharacters();

    private final String code;

    protected GroupInvitationCode() {
        this.code = null;
    }

    private GroupInvitationCode(String code) {
        validate(code);
        this.code = code;
    }

    public static GroupInvitationCode from(String code) {
        return new GroupInvitationCode(code);
    }

    public String value() {
        return code;
    }

    private void validate(String code) {
        validateNotEmpty(code);
        validateLength(code);
        validateCharacters(code);
    }

    private void validateNotEmpty(String code) {
        if (code == null || code.isBlank()) {
            throw new GroupException(GroupErrorCode.INVITE_CODE_NOT_EMPTY);
        }
    }

    private void validateLength(String code) {
        if (code.length() != CODE_LENGTH) {
            throw new GroupException(GroupErrorCode.INVALID_INVITE_CODE_LENGTH);
        }
    }

    private void validateCharacters(String code) {
        for (char c : code.toCharArray()) {
            if (!ALLOWED_CHARACTERS.contains(c)) {
                throw new GroupException(GroupErrorCode.INVALID_INVITE_CODE_ALPHA_NUMERIC_ONLY);
            }
        }
    }

    private static Set<Character> createAllowedCharacters() {
        Set<Character> letters = IntStream.rangeClosed('A', 'Z')
                .mapToObj(c -> (char) c)
                .collect(Collectors.toSet());

        Set<Character> digits = IntStream.rangeClosed('0', '9')
                .mapToObj(c -> (char) c)
                .collect(Collectors.toSet());

        letters.addAll(digits);
        return letters;
    }

    @Override
    public String toString() {
        return code;
    }
}
