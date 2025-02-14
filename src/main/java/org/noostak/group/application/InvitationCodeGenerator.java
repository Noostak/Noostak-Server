package org.noostak.group.application;

import lombok.RequiredArgsConstructor;
import org.noostak.group.common.exception.GroupErrorCode;
import org.noostak.group.common.exception.GroupException;
import org.noostak.group.domain.InvitationCodePolicy;
import org.noostak.group.domain.vo.GroupInvitationCode;

import java.security.SecureRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
public class InvitationCodeGenerator {

    private final SecureRandom random;
    private final InvitationCodePolicy policy;

    public GroupInvitationCode generate() {
        String randomCode = generateAlphaNumericCode(policy.codeLength());
        validateCode(randomCode);
        return GroupInvitationCode.from(randomCode);
    }

    private String generateAlphaNumericCode(int length) {
        String allowedChars = policy.allowedCharacters();
        return IntStream.range(0, length)
                .map(i -> random.nextInt(allowedChars.length()))
                .mapToObj(allowedChars::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());
    }

    private void validateCode(String code) {
        if (!policy.validate(code)) {
            throw new GroupException(GroupErrorCode.INVALID_INVITE_CODE_ALPHA_NUMERIC_ONLY);
        }
    }
}
