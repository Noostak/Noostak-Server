package org.noostak.membergroup.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberGroupRepository extends JpaRepository<MemberGroup, Long> {

    List<MemberGroup> findByMemberId(Long memberId);
}
