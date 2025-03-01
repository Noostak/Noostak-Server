package org.noostak.group.domain;

import org.noostak.group.common.exception.GroupErrorCode;
import org.noostak.group.common.exception.GroupException;
import org.noostak.group.domain.vo.GroupInvitationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    Optional<Group> findByCode(GroupInvitationCode groupInvitationCode);

    default Group findByCode(String groupInvitationCode){
        return findByCode(GroupInvitationCode.from(groupInvitationCode))
                .orElseThrow(()->new GroupException(GroupErrorCode.GROUP_NOT_FOUND_BY_CODE));
    }
}
