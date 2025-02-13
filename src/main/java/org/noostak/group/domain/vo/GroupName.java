package org.noostak.group.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import org.noostak.group.common.exception.GroupErrorCode;
import org.noostak.group.common.exception.GroupException;

@Embeddable
@EqualsAndHashCode
public class GroupName {
    private static final String NAME_PATTERN = "^[가-힣a-zA-Z0-9]+$";

    private final String name;

    protected GroupName() {
        this.name = null;
    }

    private GroupName(String name) {
        validateGroupName(name);
        this.name = name;
    }

    public static GroupName from(String name) {
        return new GroupName(name);
    }

    public String value() {
        return name;
    }

    private void validateGroupName(String name) {
        validateEmpty(name);
        validateLength(name);
        validatePattern(name);
    }

    private void validateEmpty(String name) {
        if (name == null || name.isBlank()) {
            throw new GroupException(GroupErrorCode.GROUP_NAME_NOT_EMPTY);
        }
    }

    private void validateLength(String name) {
        if (name.length() > 30) {
            throw new GroupException(GroupErrorCode.INVALID_GROUP_NAME_LENGTH);
        }
    }

    private void validatePattern(String name) {
        if (!name.matches(NAME_PATTERN)) {
            throw new GroupException(GroupErrorCode.INVALID_GROUP_NAME_CHARACTER);
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
