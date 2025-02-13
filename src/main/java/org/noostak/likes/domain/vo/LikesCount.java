package org.noostak.likes.domain.vo;


import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import org.noostak.likes.common.LikesErrorCode;
import org.noostak.likes.common.LikesException;

@Embeddable
@EqualsAndHashCode
public class LikesCount {
    private static final Long MAX_LIKES = 50L;

    private final Long count;

    protected LikesCount() {
        this.count = 0L;
    }

    private LikesCount(Long count) {
        validate(count);
        this.count = count;
    }

    public static LikesCount from(Long count) {
        return new LikesCount(count);
    }

    public Long value() {
        return count;
    }

    public LikesCount increase() {
        Long updatedCount = this.count + 1;
        validate(updatedCount);
        return new LikesCount(updatedCount);
    }

    public LikesCount decrease() {
        Long updatedCount = Math.max(0,this.count - 1);
        return new LikesCount(updatedCount);
    }

    private void validate(Long count) {
        validateNonNegative(count);
        validateMaxLimit(count);
    }

    private void validateNonNegative(Long count) {
        if (count < 0) {
            throw new LikesException(LikesErrorCode.LIKES_NOT_NEGATIVE);
        }
    }

    private void validateMaxLimit(Long count) {
        if (count > MAX_LIKES) {
            throw new LikesException(LikesErrorCode.OVER_MAX_LIKES,MAX_LIKES);
        }
    }

    @Override
    public String toString() {
        return String.valueOf(count);
    }
}
