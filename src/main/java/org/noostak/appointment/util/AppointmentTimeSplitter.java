package org.noostak.appointment.util;

import org.noostak.appointment.common.exception.AppointmentErrorCode;
import org.noostak.appointment.common.exception.AppointmentException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentTimeSplitter {

    public static List<TimeSlot> splitTimeSlots(LocalDateTime date, LocalDateTime startTime, LocalDateTime endTime, int durationMinutes) {
        if (durationMinutes <= 0) {
            throw new AppointmentException(AppointmentErrorCode.INVALID_DURATION);
        }

        List<TimeSlot> timeSlots = new ArrayList<>();
        LocalDateTime slotStart = startTime;

        while (slotStart.isBefore(endTime)) {
            LocalDateTime slotEnd = slotStart.plusMinutes(durationMinutes);
            if (slotEnd.isAfter(endTime)) {
                slotEnd = endTime;
            }

            timeSlots.add(TimeSlot.of(date, slotStart, slotEnd));
            slotStart = slotEnd;
        }

        return timeSlots;
    }
}