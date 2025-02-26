package org.noostak.member.domain;

import org.noostak.member.common.MemberErrorCode;
import org.noostak.member.common.MemberException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    default Member getById(Long memberId){
        return findById(memberId)
                .orElseThrow(()->new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
    }
}
