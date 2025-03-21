package org.noostak.appointment.application;

import org.noostak.appointment.dto.calendar.CalendarResponse;

public interface CalendarService{
    CalendarResponse getCalendarViewByGroupId(Long memberId, Long groupId, int year, int month);
}
