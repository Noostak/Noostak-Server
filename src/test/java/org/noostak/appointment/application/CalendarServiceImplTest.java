package org.noostak.appointment.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.noostak.appointment.domain.Appointment;
import org.noostak.appointment.domain.AppointmentRepository;
import org.noostak.appointment.domain.vo.AppointmentCategory;
import org.noostak.appointment.domain.vo.AppointmentStatus;
import org.noostak.appointment.dto.calendar.CalendarResponse;
import org.noostak.appointment.dto.calendar.MonthAppointment;
import org.noostak.appointment.dto.calendar.MonthAppointments;
import org.noostak.appointmentoption.domain.AppointmentOption;
import org.noostak.appointmentoption.domain.AppointmentOptionRepository;
import org.noostak.appointmentoption.domain.vo.AppointmentOptionStatus;
import org.noostak.group.domain.Group;
import org.noostak.group.domain.vo.GroupInvitationCode;
import org.noostak.group.domain.vo.GroupName;
import org.noostak.group.domain.vo.GroupProfileImageKey;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CalendarServiceImplTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private AppointmentOptionRepository appointmentOptionRepository;

    @InjectMocks
    private CalendarServiceImpl calendarService;

    private List<Appointment> testAppointments;
    private List<AppointmentOption> testAppointmentOptions;

    private Long groupId = 1L;
    private int year = 2025;
    private int month = 2; // 2월
    private Group testGroup;

    @BeforeEach
    void setUp() throws Exception {
        // 테스트 그룹 생성
        testGroup = createTestGroup();

        // 테스트 Appointment 데이터 생성
        testAppointments = createTestAppointments();

        // 테스트 AppointmentOption 데이터 생성
        testAppointmentOptions = createTestAppointmentOptions();

        // appointmentRepository.findAllByGroupId Mock 설정
        when(appointmentRepository.getAllByGroupIdConfirmed(AppointmentStatus.CONFIRMED,groupId)).thenReturn(testAppointments);

        // appointmentOptionRepository.getByAppointmentConfirmedYearAndMonth Mock 설정
        for (int i = 0; i < testAppointments.size(); i++) {
            Appointment appointment = testAppointments.get(i);
            AppointmentOption option = testAppointmentOptions.get(i);

            when(appointmentOptionRepository.getByAppointmentConfirmedYearAndMonth(
                    eq(appointment), eq(year), eq(month)
            )).thenReturn(option);
        }

        // appointmentOptionRepository.getAllByAppointmentConfirmedBetweenDate Mock 설정
        for (int i = 0; i < testAppointments.size(); i++) {
            Appointment appointment = testAppointments.get(i);
            AppointmentOption option = testAppointmentOptions.get(i);

            when(appointmentOptionRepository.getAllByAppointmentConfirmedBetweenDate(
                    eq(appointment), any(LocalDate.class), any(LocalDate.class)
            )).thenReturn(option);
        }
    }

    @Test
    @DisplayName("그룹 ID로 캘린더 정보를 가져온다")
    void getCalendarViewByGroupId_ShouldReturnCalendarResponse() {
        // given
        // 셋업에서 준비된 mock 객체 및 테스트 데이터 사용

        // when
        CalendarResponse response = calendarService.getCalendarViewByGroupId(groupId, year, month);
        System.out.println(response.toString());
        // then
        assertThat(response).isNotNull();
        assertThat(response.getYear()).isEqualTo(year);
        assertThat(response.getMonth()).isEqualTo(month);

        // 현재 달의 약속 검증
        assertThat(response.getCurrentMonthAppointments()).isNotEmpty();

        // 이전 달의 약속 검증
        assertThat(response.getPreviousMonthAppointments()).isNotEmpty();

        // 하루에 해당하는 약속들 검증 (첫 번째 날짜)
        if (!response.getCurrentMonthAppointments().isEmpty()) {
            MonthAppointments dayAppointments = response.getCurrentMonthAppointments().get(0);
            assertThat(dayAppointments.getAppointments()).isNotEmpty();

            MonthAppointment firstAppointment = dayAppointments.getAppointments().get(0);
            assertThat(firstAppointment.getId()).isNotNull();
            assertThat(firstAppointment.getName()).isNotEmpty();
            assertThat(firstAppointment.getDate()).isNotNull();
            assertThat(firstAppointment.getStartTime()).isNotNull();
            assertThat(firstAppointment.getEndTime()).isNotNull();
            assertThat(firstAppointment.getDuration()).isGreaterThan(0);
        }
    }

    private Group createTestGroup() throws Exception {
        // Group 객체 생성 (팩토리 메서드 사용)
        GroupName groupName = GroupName.from("테스트그룹");
        GroupProfileImageKey profileImageKey = GroupProfileImageKey.from("test-image-key");
        String invitationCode = "TEST12";

        Group group = Group.of(1L, groupName, profileImageKey, invitationCode);

        // ID 필드 설정 (Reflection 사용)
        Field idField = Group.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(group, groupId);

        return group;
    }

    private List<Appointment> createTestAppointments() {
        List<Appointment> appointments = new ArrayList<>();

        // 약속 1: 팀 미팅
        Appointment appointment1 = Appointment.of(
                testGroup,
                1L,
                "팀 미팅",
                60L,
                "중요",
                AppointmentStatus.CONFIRMED
        );
        setAppointmentId(appointment1, 1L);

        // 약속 2: 고객 상담
        Appointment appointment2 = Appointment.of(
                testGroup,
                1L,
                "고객 상담",
                60L,
                "일정",
                AppointmentStatus.CONFIRMED
        );
        setAppointmentId(appointment2, 2L);

        // 약속 3: 프로젝트 리뷰
        Appointment appointment3 = Appointment.of(
                testGroup,
                1L,
                "프로젝트 리뷰",
                120L,
                "중요",
                AppointmentStatus.CONFIRMED
        );
        setAppointmentId(appointment3, 3L);

        appointments.add(appointment1);
        appointments.add(appointment2);
        appointments.add(appointment3);

        return appointments;
    }

    private void setAppointmentId(Appointment appointment, Long id) {
        try {
            Field idField = Appointment.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(appointment, id);
        } catch (Exception e) {
            throw new RuntimeException("Appointment ID 설정 중 오류 발생", e);
        }
    }

    private List<AppointmentOption> createTestAppointmentOptions() {
        List<AppointmentOption> options = new ArrayList<>();

        // 약속 옵션 1: 2월 10일
        LocalDateTime date1 = LocalDateTime.of(2025, 2, 10, 0, 0);
        AppointmentOption option1 = createAppointmentOption(1L, testAppointments.get(0),
                date1,
                LocalDateTime.of(2025, 2, 10, 14, 0),
                LocalDateTime.of(2025, 2, 10, 15, 0));

        // 약속 옵션 2: 2월 15일
        LocalDateTime date2 = LocalDateTime.of(2025, 2, 15, 0, 0);
        AppointmentOption option2 = createAppointmentOption(2L, testAppointments.get(1),
                date2,
                LocalDateTime.of(2025, 2, 15, 10, 0),
                LocalDateTime.of(2025, 2, 15, 10, 30));

        // 약속 옵션 3: 2월 20일
        LocalDateTime date3 = LocalDateTime.of(2025, 2, 20, 0, 0);
        AppointmentOption option3 = createAppointmentOption(3L, testAppointments.get(2),
                date3,
                LocalDateTime.of(2025, 2, 20, 15, 0),
                LocalDateTime.of(2025, 2, 20, 16, 30));

        options.add(option1);
        options.add(option2);
        options.add(option3);

        return options;
    }

    private AppointmentOption createAppointmentOption(Long id, Appointment appointment,
                                                      LocalDateTime date,
                                                      LocalDateTime startTime,
                                                      LocalDateTime endTime) {
        AppointmentOption option = new AppointmentOption();

        // Reflection을 사용하여 private 필드 설정 (테스트 목적)
        try {
            Field idField = AppointmentOption.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(option, id);

            Field appointmentField = AppointmentOption.class.getDeclaredField("appointment");
            appointmentField.setAccessible(true);
            appointmentField.set(option, appointment);

            Field dateField = AppointmentOption.class.getDeclaredField("date");
            dateField.setAccessible(true);
            dateField.set(option, date);

            Field startTimeField = AppointmentOption.class.getDeclaredField("startTime");
            startTimeField.setAccessible(true);
            startTimeField.set(option, startTime);

            Field endTimeField = AppointmentOption.class.getDeclaredField("endTime");
            endTimeField.setAccessible(true);
            endTimeField.set(option, endTime);

            Field statusField = AppointmentOption.class.getDeclaredField("status");
            statusField.setAccessible(true);
            statusField.set(option, AppointmentOptionStatus.CONFIRMED);

        } catch (Exception e) {
            throw new RuntimeException("테스트 AppointmentOption 객체 생성 중 오류 발생", e);
        }

        return option;
    }
}