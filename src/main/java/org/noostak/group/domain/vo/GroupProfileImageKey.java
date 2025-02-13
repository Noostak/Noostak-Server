package org.noostak.group.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class GroupProfileImageKey {

    private final String key;

    protected GroupProfileImageKey() {
        this.key = null;
    }

    private GroupProfileImageKey(String key) {
        this.key = key;
    }

    public static GroupProfileImageKey from(String key) {
        if (key == null) {
            return new GroupProfileImageKey();
        }
        return new GroupProfileImageKey(key);
    }

    public String value() {
        return key;
    }

    @Override
    public String toString() {
        return key;
    }
}
