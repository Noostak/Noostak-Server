package org.noostak.membergroup.domain;

import org.noostak.group.domain.Group;
import java.util.Collections;
import java.util.List;

public class MemberGroups {
    private final List<MemberGroup> memberGroups;

    private MemberGroups(List<MemberGroup> memberGroups) {
        this.memberGroups = memberGroups;
    }

    public static MemberGroups of(final List<MemberGroup> memberGroups) {
        return new MemberGroups(memberGroups);
    }

    public List<MemberGroup> getMemberGroups() {
        return Collections.unmodifiableList(memberGroups);
    }

    public List<Group> toGroups() {
        return memberGroups.stream()
                .map(MemberGroup::getGroup)
                .toList();
    }

    public boolean isEmpty() {
        return memberGroups.isEmpty();
    }
}
