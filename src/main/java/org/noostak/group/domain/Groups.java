package org.noostak.group.domain;

import java.util.Collections;
import java.util.List;

public class Groups {
    private final List<Group> groups;

    private Groups(List<Group> groups) {
        this.groups = groups;
    }

    public static Groups of(final List<Group> groups) {
        return new Groups(groups);
    }

    public List<Group> getGroups() {
        return Collections.unmodifiableList(groups);
    }
}
