package org.noostak.member.application;

import org.noostak.member.domain.Member;
import org.noostak.auth.dto.common.SignUpRequest;
import org.noostak.member.dto.GetProfileResponse;
import org.noostak.member.dto.UpdateProfileRequest;
import org.springframework.web.multipart.MultipartFile;

public interface MemberService {
    // create
    Member createMember(SignUpRequest request);

    // read
    GetProfileResponse fetchMember(Long memberId);

    // update
    void updateMember(Long memberId, UpdateProfileRequest dto);

    // delete
    void deleteMember(Long memberId);

}
