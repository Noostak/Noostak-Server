package org.noostak.member.application;

import org.noostak.member.domain.Member;
import org.noostak.auth.dto.SignUpRequest;
import org.noostak.member.dto.GetProfileResponse;

public interface MemberService {
    // create
    Member createMember(SignUpRequest request);

    // read
    GetProfileResponse fetchMember(Long memberId);

    // update
    void updateMember();

    // delete
    void deleteMember();

}
