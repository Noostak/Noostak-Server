package org.noostak.appointment.application;


import lombok.RequiredArgsConstructor;
import org.noostak.appointment.domain.Appointment;
import org.noostak.appointment.domain.AppointmentRepository;
import org.noostak.appointment.domain.vo.AppointmentStatus;
import org.noostak.appointment.dto.calendar.CalendarResponse;
import org.noostak.appointment.dto.calendar.MonthAppointment;
import org.noostak.appointment.dto.calendar.MonthAppointments;
import org.noostak.appointmentoption.domain.AppointmentOption;
import org.noostak.appointmentoption.domain.AppointmentOptionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CalendarServiceImpl implements CalendarService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentOptionRepository appointmentOptionRepository;


    @Override
    public CalendarResponse getCalendarViewByGroupId(Long groupId, int year, int month) {

        List<Appointment> appointmentList =
                appointmentRepository.getAllByGroupIdConfirmed(AppointmentStatus.CONFIRMED,groupId);

        // 이번 달의 캘린더 정보 목록 불러오기
        ArrayList<MonthAppointments> currentMonthAppointments =
                getMonthAppointments(appointmentList, year, month);

        // 이전 달의 캘린더 정보 목록 불러오기
        ArrayList<MonthAppointments> previousMonthAppointments =
                getPreviousMonthAppointments(appointmentList, year, month);

        return CalendarResponse.of(year, month, currentMonthAppointments, previousMonthAppointments);
    }

    private ArrayList<MonthAppointments> getPreviousMonthAppointments(List<Appointment> appointmentList, int year, int month) {
        LocalDate firstDate = LocalDate.of(year, month, 1);
        int weekNumber = firstDate.getDayOfWeek().getValue();
        LocalDate previousDate = firstDate.minusDays(weekNumber);

        return getMonthAppointments(appointmentList, firstDate, previousDate);
    }

    private ArrayList<MonthAppointments> getMonthAppointments(List<Appointment> appointmentList, LocalDate firstDate, LocalDate previousDate) {
        HashMap<Integer, ArrayList<MonthAppointment>> previousMonthAppointmentMapper =
                getMonthAppointmentsMapper(appointmentList, firstDate, previousDate);

        return getMonthAppointments(previousMonthAppointmentMapper);
    }

    private ArrayList<MonthAppointments> getMonthAppointments(List<Appointment> appointmentList, int year, int month) {
        HashMap<Integer, ArrayList<MonthAppointment>> currentMonthAppointmentMapper =
                getMonthAppointmentsMapper(appointmentList, year, month);

        return getMonthAppointments(currentMonthAppointmentMapper);
    }

    private HashMap<Integer, ArrayList<MonthAppointment>> getMonthAppointmentsMapper(List<Appointment> appointmentList, LocalDate firstDate, LocalDate previousDate) {
        HashMap<Integer, ArrayList<MonthAppointment>> previousMonthAppointmentMapper = new HashMap<>();

        for (Appointment appointment : appointmentList) {
            AppointmentOption previousMonthAppointmentOption
                    = getAppointmentConfirmed(firstDate, previousDate, appointment);

            int day = previousMonthAppointmentOption.getDayOfMonth();

            ArrayList<MonthAppointment> dayOfOptions =
                    previousMonthAppointmentMapper.getOrDefault(day, new ArrayList<>());

            MonthAppointment previousMonthAppointment =
                    MonthAppointment.from(appointment, previousMonthAppointmentOption);

            dayOfOptions.add(previousMonthAppointment);

            previousMonthAppointmentMapper.put(day, dayOfOptions);
        }
        return previousMonthAppointmentMapper;
    }

    private HashMap<Integer, ArrayList<MonthAppointment>>
    getMonthAppointmentsMapper(List<Appointment> appointmentList, int year, int month) {
        HashMap<Integer, ArrayList<MonthAppointment>> MonthAppointmentMapper = new HashMap<>();

        for (Appointment appointment : appointmentList) {

            AppointmentOption appointmentOption =
                    getAppointmentConfirmed(appointment, year, month);

            int day = appointmentOption.getDayOfMonth();

            ArrayList<MonthAppointment> dayOfOptions = MonthAppointmentMapper.getOrDefault(day, new ArrayList<>());

            MonthAppointment monthAppointment = MonthAppointment.from(appointment, appointmentOption);
            dayOfOptions.add(monthAppointment);

            MonthAppointmentMapper.put(day, dayOfOptions);
        }

        return MonthAppointmentMapper;
    }

    private static ArrayList<MonthAppointments> getMonthAppointments
            (HashMap<Integer, ArrayList<MonthAppointment>> monthAppointmentMapper) {
        ArrayList<MonthAppointments> currentMonthAppointments = new ArrayList<>();

        for (int day : monthAppointmentMapper.keySet()) {
            ArrayList<MonthAppointment> appointmentOptions = monthAppointmentMapper.get(day);
            MonthAppointments monthAppointments = new MonthAppointments(day, appointmentOptions);

            currentMonthAppointments.add(monthAppointments);
            Collections.sort(appointmentOptions);
        }

        return currentMonthAppointments;
    }

    private AppointmentOption getAppointmentConfirmed(Appointment appointment, int year, int month) {
        return appointmentOptionRepository.
                getByAppointmentConfirmedYearAndMonth(
                        appointment, year, month);
    }

    private AppointmentOption getAppointmentConfirmed(LocalDate firstDate, LocalDate previousDate, Appointment appointment) {
        return appointmentOptionRepository
                .getAllByAppointmentConfirmedBetweenDate(appointment, previousDate, firstDate);
    }
}
