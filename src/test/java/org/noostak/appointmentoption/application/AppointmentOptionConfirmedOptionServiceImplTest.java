package org.noostak.appointmentoption.application;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Nested;
import org.noostak.appointment.domain.Appointment;
import org.noostak.appointment.domain.AppointmentRepositoryTest;
import org.noostak.appointmentmember.domain.AppointmentMember;
import org.noostak.appointmentmember.domain.repository.AppointmentMemberRepositoryTest;
import org.noostak.appointmentmember.domain.vo.AppointmentAvailability;
import org.noostak.appointmentoption.common.exception.AppointmentOptionException;
import org.noostak.appointmentoption.domain.AppointmentOption;
import org.noostak.appointmentoption.domain.AppointmentOptionRepositoryTest;
import org.noostak.appointmentoption.dto.response.confirmed.AppointmentConfirmedOptionResponse;
import org.noostak.group.domain.Group;
import org.noostak.group.domain.GroupRepositoryTest;
import org.noostak.group.domain.vo.GroupName;
import org.noostak.group.domain.vo.GroupProfileImageKey;
import org.noostak.member.domain.Member;
import org.noostak.member.domain.vo.MemberName;
import org.noostak.member.domain.vo.MemberProfileImageKey;
import org.noostak.member.MemberRepositoryTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AppointmentOptionConfirmedOptionServiceImplTest {

    private AppointmentOptionConfirmedOptionServiceImpl service;
    private AppointmentOptionRepositoryTest appointmentOptionRepository;
    private AppointmentMemberRepositoryTest appointmentMemberRepository;
    private AppointmentRepositoryTest appointmentRepository;
    private GroupRepositoryTest groupRepository;
    private MemberRepositoryTest memberRepository;

    private Long savedMemberId;
    private Long savedAppointmentOptionId;

    @BeforeEach
    void setUp() {
        groupRepository = new GroupRepositoryTest();
        memberRepository = new MemberRepositoryTest();
        appointmentRepository = new AppointmentRepositoryTest();
        appointmentOptionRepository = new AppointmentOptionRepositoryTest();
        appointmentMemberRepository = new AppointmentMemberRepositoryTest();

        service = new AppointmentOptionConfirmedOptionServiceImpl(appointmentOptionRepository, appointmentMemberRepository);
        initializeTestData();
    }

    @AfterEach
    void tearDown() {
        appointmentMemberRepository.deleteAll();
        appointmentOptionRepository.deleteAll();
        appointmentRepository.deleteAll();
        memberRepository.deleteAll();
        groupRepository.deleteAll();
    }

    private void initializeTestData() {
        Group group = createAndSaveGroup();
        Member member = createAndSaveMember("사용자One");
        savedMemberId = member.getId();
        Appointment appointment = createAndSaveAppointment(group);
        AppointmentOption appointmentOption = createAndSaveAppointmentOption(appointment);
        savedAppointmentOptionId = appointmentOption.getId();
        createAndSaveAppointmentMember(appointment, member, AppointmentAvailability.AVAILABLE);
    }

    @Nested
    @DisplayName("성공 케이스")
    class SuccessCases {

        @Test
        @DisplayName("확정된 약속 옵션을 정상적으로 조회할 수 있다.")
        void shouldGetConfirmedAppointmentOptionSuccessfully() {
            AppointmentConfirmedOptionResponse response = service.getConfirmedAppointmentOption(savedMemberId, savedAppointmentOptionId);

            assertThat(response).isNotNull();
            assertThat(response.availableFriends().names()).containsExactly("사용자One");
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class FailureCases {

        @Test
        @DisplayName("존재하지 않는 약속 옵션을 조회하면 예외가 발생한다.")
        void shouldThrowExceptionWhenAppointmentOptionNotFound() {
            Long nonExistentOptionId = 999L;
            assertThatThrownBy(() -> service.getConfirmedAppointmentOption(savedMemberId, nonExistentOptionId))
                    .isInstanceOf(AppointmentOptionException.class);
        }

        @Test
        @DisplayName("해당 멤버가 약속에 포함되지 않았을 경우 예외가 발생한다.")
        void shouldThrowExceptionWhenMemberNotFound() {
            Long nonExistentMemberId = 999L;
            assertThatThrownBy(() -> service.getConfirmedAppointmentOption(nonExistentMemberId, savedAppointmentOptionId))
                    .isInstanceOf(AppointmentOptionException.class);
        }
    }

    private Group createAndSaveGroup() {
        return groupRepository.save(
                Group.of(1L, GroupName.from("테스트그룹"), GroupProfileImageKey.from("group-image-key"), "INVITE"));
    }

    private Member createAndSaveMember(String name) {
        return memberRepository.save(Member.of(MemberName.from(name), MemberProfileImageKey.from("profile-key")));
    }

    private Appointment createAndSaveAppointment(Group group) {
        return appointmentRepository.save(Appointment.of(group, 1L, "테스트 약속", 60L, "중요", org.noostak.appointment.domain.vo.AppointmentStatus.PROGRESS));
    }

    private AppointmentOption createAndSaveAppointmentOption(Appointment appointment) {
        AppointmentOption appointmentOption = AppointmentOption.of(
                appointment,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2)
        );

        appointmentOption.confirm();

        AppointmentOption savedOption = appointmentOptionRepository.save(appointmentOption);
        appointmentOptionRepository.flush();

        return appointmentOptionRepository.findById(savedOption.getId())
                .orElseThrow(() -> new RuntimeException("Saved AppointmentOption not found!"));
    }

    private void createAndSaveAppointmentMember(Appointment appointment, Member member, AppointmentAvailability availability) {
        appointmentMemberRepository.save(AppointmentMember.of(availability, appointment, member));
    }
}
