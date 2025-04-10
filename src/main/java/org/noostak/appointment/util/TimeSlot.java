package org.noostak.appointment.util;

import org.noostak.global.utils.TimeInterval;

import java.time.LocalDateTime;

public record TimeSlot(LocalDateTime date, LocalDateTime start, LocalDateTime end) implements TimeInterval {

    public static TimeSlot of(LocalDateTime date, LocalDateTime start, LocalDateTime end) {
        return new TimeSlot(date, start, end);
    }

    @Override
    public LocalDateTime getStartTime() {
        return start();
    }

    @Override
    public LocalDateTime getEndTime() {
        return end();
    }
}
