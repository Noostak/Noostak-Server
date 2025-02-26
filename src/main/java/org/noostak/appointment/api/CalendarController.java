package org.noostak.appointment.api;


import lombok.RequiredArgsConstructor;
import org.noostak.appointment.application.CalendarService;
import org.noostak.appointment.common.success.CalendarSuccessCode;
import org.noostak.appointment.dto.calendar.CalendarResponse;
import org.noostak.global.success.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CalendarController {

    private final CalendarService calendarService;
    @GetMapping("/api/v1/groups/{groupId}/appointments/calendar")
    public ResponseEntity<SuccessResponse> getCalendar(
            @PathVariable Long groupId,
            @RequestParam int year,
            @RequestParam int month
    ){
        CalendarResponse response = calendarService.getCalendarViewByGroupId(groupId, year, month);
        return ResponseEntity.ok(SuccessResponse.of(CalendarSuccessCode.CALENDAR_CREATED,response));
    }
}
