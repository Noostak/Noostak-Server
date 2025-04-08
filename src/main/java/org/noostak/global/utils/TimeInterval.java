package org.noostak.global.utils;

import org.noostak.global.error.core.TimeEntityErrorCode;
import org.noostak.global.error.core.TimeEntityException;

import java.time.LocalDateTime;

public interface TimeInterval {
    default boolean conflictsWith(TimeInterval otherEntity) {
        return containsTimeEntity(otherEntity)
                || containsTimeEntity(this);
    }

    default boolean containsTimeEntity(TimeInterval otherEntity){
        return this.containsTime(otherEntity.getStartTime())
                || this.containsTime(otherEntity.getEndTime());
    }

    default boolean containsTime(LocalDateTime time) {
        return this.getStartTime().isBefore(time)
                && this.getEndTime().isAfter(time);
    }

    default void validate(){
        if(this.getEndTime().isBefore(getStartTime())){
            throw new TimeEntityException(TimeEntityErrorCode.INVALID_TIME);
        }
    }

    default boolean isFullyContainedIn(LocalDateTime startTime, LocalDateTime endTime) {
        return ((startTime.isBefore(this.getStartTime()) || startTime.isEqual(this.getStartTime()))
                && (endTime.isAfter(this.getStartTime()))) &&
                (startTime.isBefore(this.getEndTime())
                        && (endTime.isAfter(this.getEndTime()) || endTime.isEqual(this.getEndTime())));
    }

    LocalDateTime getStartTime();

    LocalDateTime getEndTime();
}
