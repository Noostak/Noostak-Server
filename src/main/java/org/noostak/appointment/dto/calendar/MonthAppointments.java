package org.noostak.appointment.dto.calendar;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MonthAppointments {
    int day;
    List<MonthAppointment> appointments;
}
