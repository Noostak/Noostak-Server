//package org.noostak.appointment.application;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.noostak.appointment.application.create.AppointmentCreateServiceImpl;
//import org.noostak.appointment.domain.Appointment;
//import org.noostak.appointment.domain.AppointmentRepositoryTest;
//import org.noostak.appointment.dto.calendar.CalendarResponse;
//import org.noostak.appointment.dto.request.AppointmentCreateRequest;
//import org.noostak.appointment.dto.request.AppointmentHostSelectionTimeRequest;
//import org.noostak.appointmentoption.application.AppointmentOptionConfirmServiceImpl;
//import org.noostak.appointmentoption.domain.AppointmentOption;
//import org.noostak.appointmentoption.domain.AppointmentOptionRepositoryTest;
//import org.noostak.group.domain.Group;
//import org.noostak.group.domain.GroupRepositoryTest;
//import org.noostak.group.domain.vo.GroupName;
//import org.noostak.group.domain.vo.GroupProfileImageKey;
//import org.noostak.member.MemberRepositoryTest;
//import org.noostak.member.domain.Member;
//import org.noostak.member.domain.vo.MemberName;
//import org.noostak.member.domain.vo.MemberProfileImageKey;
//import org.noostak.membergroup.MemberGroupRepositoryTest;
//import org.noostak.membergroup.domain.MemberGroup;
//import java.time.LocalDateTime;
//import java.util.List;
//import static org.assertj.core.api.Assertions.assertThat;
//
//class CalendarServiceImplTest {
//
//    private AppointmentRepositoryTest appointmentRepository;
//    private AppointmentOptionRepositoryTest appointmentOptionRepository;
//    private AppointmentCreateServiceImpl appointmentCreateService;
//    private AppointmentOptionConfirmServiceImpl appointmentOptionConfirmService;
//    private CalendarServiceImpl calendarService;
//
//    private MemberRepositoryTest memberRepository;
//    private GroupRepositoryTest groupRepository;
//    private MemberGroupRepositoryTest memberGroupRepository;
//
//    private Long groupId;
//    private Long memberId;
//    private int year = 2025;
//    private int month = 2; // 2월
//
//    @BeforeEach
//    void setUp() {
//        memberRepository = new MemberRepositoryTest();
//        groupRepository = new GroupRepositoryTest();
//        memberGroupRepository = new MemberGroupRepositoryTest();
//        appointmentRepository = new AppointmentRepositoryTest();
//        appointmentOptionRepository = new AppointmentOptionRepositoryTest();
//
//        appointmentCreateService = new AppointmentCreateServiceImpl(memberGroupRepository, groupRepository, appointmentRepository, null);
//        appointmentOptionConfirmService = new AppointmentOptionConfirmServiceImpl(appointmentOptionRepository);
//        calendarService = new CalendarServiceImpl(appointmentRepository, appointmentOptionRepository);
//
//        // 멤버 및 그룹 생성
//        memberId = createAndSaveMember("사용자One");
//        groupId = createAndSaveGroup(memberId, "테스트그룹");
//
//        saveMemberGroup(memberId, groupId);
//
//        // 약속 생성 및 저장
//        createTestAppointments();
//
//        // 약속 옵션 생성 및 저장
//        createTestAppointmentOptions();
//    }
//
//    @Test
//    @DisplayName("그룹 ID로 캘린더 정보를 가져온다 (모킹 없이)")
//    void getCalendarViewByGroupId_ShouldReturnCalendarResponse() {
//        // when
//        CalendarResponse response = calendarService.getCalendarViewByGroupId(groupId, year, month);
//
//        // then
//        assertThat(response).isNotNull();
//        assertThat(response.getYear()).isEqualTo(year);
//        assertThat(response.getMonth()).isEqualTo(month);
//        assertThat(response.getCurrentMonthAppointments()).isNotEmpty();
//        assertThat(response.getPreviousMonthAppointments()).isNotEmpty();
//    }
//
//    private void createTestAppointments() {
//        List<AppointmentHostSelectionTimeRequest> selectionTimes = List.of(
//                AppointmentHostSelectionTimeRequest.of(
//                        LocalDateTime.of(2025, 2, 10, 10, 0),
//                        LocalDateTime.of(2025, 2, 10, 11, 0),
//                        LocalDateTime.of(2025, 2, 10, 12, 0)
//                )
//        );
//
//        AppointmentCreateRequest request1 = AppointmentCreateRequest.of("팀 미팅", "중요", 60L, selectionTimes);
//        AppointmentCreateRequest request2 = AppointmentCreateRequest.of("고객 상담", "일정", 60L, selectionTimes);
//        AppointmentCreateRequest request3 = AppointmentCreateRequest.of("프로젝트 리뷰", "중요", 120L, selectionTimes);
//
//        appointmentCreateService.createAppointment(memberId, groupId, request1);
//        appointmentCreateService.createAppointment(memberId, groupId, request2);
//        appointmentCreateService.createAppointment(memberId, groupId, request3);
//    }
//
//    private void createTestAppointmentOptions() {
//        List<Appointment> appointments = appointmentRepository.findAll();
//
//        for (Appointment appointment : appointments) {
//            AppointmentOption option = AppointmentOption.of(
//                    appointment,
//                    LocalDateTime.of(2025, 2, 10, 0, 0),
//                    LocalDateTime.of(2025, 2, 10, 14, 0),
//                    LocalDateTime.of(2025, 2, 10, 15, 0)
//            );
//            option.confirm();
//            appointmentOptionRepository.save(option);
//            appointmentOptionConfirmService.confirmAppointment(option.getId());
//        }
//    }
//
//    private Long createAndSaveMember(String name) {
//        return memberRepository.save(Member.of(
//                MemberName.from(name),
//                MemberProfileImageKey.from("profile-key")
//        )).getId();
//    }
//
//    private Long createAndSaveGroup(Long memberId, String groupName) {
//        return groupRepository.save(Group.of(
//                memberId,
//                GroupName.from(groupName),
//                GroupProfileImageKey.from("group-image-key"),
//                "INVITE"
//        )).getId();
//    }
//
//    private void saveMemberGroup(Long memberId, Long groupId) {
//        Group group = groupRepository.findById(groupId)
//                .orElseThrow(() -> new RuntimeException("Group not found for ID: " + groupId));
//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(() -> new RuntimeException("Member not found for ID: " + memberId));
//
//        memberGroupRepository.save(MemberGroup.of(member, group));
//    }
//}
