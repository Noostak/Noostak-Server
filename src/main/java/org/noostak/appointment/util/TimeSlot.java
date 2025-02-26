package org.noostak.appointment.util;

import java.time.LocalDateTime;

public record TimeSlot(LocalDateTime date, LocalDateTime start, LocalDateTime end) {

    public static TimeSlot of(LocalDateTime date, LocalDateTime start, LocalDateTime end) {
        return new TimeSlot(date, start, end);
    }
}
