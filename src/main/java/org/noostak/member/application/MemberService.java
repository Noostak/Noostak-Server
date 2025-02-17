package org.noostak.member.application;

import org.noostak.member.domain.Member;
import org.noostak.auth.dto.SignUpRequest;

public interface MemberService {
    // create
    Member createMember(SignUpRequest request);

    // read
    void fetchMember();

    // update
    void updateMember();

    // delete
    void deleteMember();

}
