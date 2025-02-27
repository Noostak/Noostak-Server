package org.noostak.appointment.dto.calendar;

import lombok.Getter;

import java.util.List;


@Getter
public class CalendarResponse {
    int year;
    int month;
    List<MonthAppointments> currentMonthAppointments;
    List<MonthAppointments> previousMonthAppointments;

    private CalendarResponse(int year, int month,
                             List<MonthAppointments> currentMonthAppointments,
                             List<MonthAppointments> previousMonthAppointments) {
        this.year = year;
        this.month = month;
        this.currentMonthAppointments = currentMonthAppointments;
        this.previousMonthAppointments = previousMonthAppointments;
    }

    public static CalendarResponse of(int year, int month,
                                      List<MonthAppointments> currentMonthAppointments,
                                      List<MonthAppointments> previousMonthAppointments){
        return new CalendarResponse(year,month,currentMonthAppointments,previousMonthAppointments);
    }
}
