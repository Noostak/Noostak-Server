package org.noostak.group.domain;

public interface InvitationCodePolicy {
    int codeLength();
    String allowedCharacters();
    boolean validate(String code);
}
