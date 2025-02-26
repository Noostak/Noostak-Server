package org.noostak.appointment.dto.calendar;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.noostak.appointment.domain.Appointment;
import org.noostak.appointmentoption.domain.AppointmentOption;
import org.noostak.appointmentoption.domain.vo.AppointmentOptionStatus;

import java.time.LocalDateTime;


@Getter
@AllArgsConstructor
public class MonthAppointment implements Comparable<MonthAppointment>{
    Long id;
    String name;
    LocalDateTime date;
    LocalDateTime startTime;
    LocalDateTime endTime;
    long duration;


    public static MonthAppointment from(Appointment appointment, AppointmentOption appointmentOption){
        if(appointmentOption.getStatus().equals(AppointmentOptionStatus.CONFIRMED)){
            // TODO : 에러메시지 수정
            throw new RuntimeException("확정된 옵션이 아닙니다.");
        }

        return new MonthAppointment(
                appointment.getId(),
                appointment.getName().value(),
                appointmentOption.getDate(),
                appointmentOption.getStartTime(),
                appointmentOption.getEndTime(),
                appointment.getDuration().value());
    }

    @Override
    public int compareTo(MonthAppointment other) {
        return this.startTime.compareTo(other.startTime);
    }
}
