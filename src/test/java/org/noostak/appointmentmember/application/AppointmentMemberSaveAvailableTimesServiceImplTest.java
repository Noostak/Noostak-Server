package org.noostak.appointmentmember.application;

import org.junit.jupiter.api.*;
import org.noostak.appointment.common.exception.AppointmentErrorCode;
import org.noostak.appointment.common.exception.AppointmentException;
import org.noostak.appointment.domain.vo.AppointmentStatus;
import org.noostak.appointmentmember.common.exception.AppointmentMemberErrorCode;
import org.noostak.appointmentmember.common.exception.AppointmentMemberException;
import org.noostak.appointmentmember.domain.AppointmentMember;
import org.noostak.appointmentmember.domain.AppointmentMemberAvailableTime;
import org.noostak.appointmentmember.domain.repository.AppointmentMemberAvailableTimesRepositoryTest;
import org.noostak.appointmentmember.domain.repository.AppointmentMemberRepositoryTest;
import org.noostak.appointmentmember.domain.vo.AppointmentAvailability;
import org.noostak.appointmentmember.dto.request.AppointmentMemberAvailableTimeRequest;
import org.noostak.appointmentmember.dto.request.AppointmentMemberAvailableTimesRequest;
import org.noostak.appointmentmember.domain.AppointmentMemberRepository;
import org.noostak.appointmentmember.domain.AppointmentMemberAvailableTimesRepository;
import org.noostak.group.domain.Group;
import org.noostak.group.domain.GroupRepository;
import org.noostak.group.domain.GroupRepositoryTest;
import org.noostak.group.domain.vo.GroupName;
import org.noostak.group.domain.vo.GroupProfileImageKey;
import org.noostak.member.MemberRepositoryTest;
import org.noostak.member.domain.Member;
import org.noostak.appointment.domain.Appointment;
import org.noostak.appointment.domain.AppointmentRepositoryTest;
import org.noostak.member.domain.vo.AuthId;
import org.noostak.member.domain.vo.AuthType;
import org.noostak.member.domain.vo.MemberName;
import org.noostak.member.domain.vo.MemberProfileImageKey;
import org.noostak.membergroup.MemberGroupRepositoryTest;
import org.noostak.membergroup.domain.MemberGroup;
import org.noostak.membergroup.domain.MemberGroupRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
public class AppointmentMemberSaveAvailableTimesServiceImplTest {

    private AppointmentMemberSaveAvailableTimesService appointmentMemberSaveAvailableTimesService;
    private AppointmentMemberRepository appointmentMemberRepository;
    private AppointmentMemberAvailableTimesRepository appointmentMemberAvailableTimesRepository;
    private MemberRepositoryTest memberRepository;
    private AppointmentRepositoryTest appointmentRepository;
    private GroupRepository groupRepository;
    private MemberGroupRepository memberGroupRepository;

    private Long savedMemberId;
    private Long savedGroupId;
    private Long savedAppointmentId;
    private AppointmentMember savedAppointmentMember;

    @BeforeEach
    void setUp() {
        initializeRepositories();
        initializeTestData();
    }

    @AfterEach
    void tearDown() {
        appointmentMemberAvailableTimesRepository.deleteAll();
        appointmentMemberRepository.deleteAll();
        appointmentRepository.deleteAll();
        memberRepository.deleteAll();
        groupRepository.deleteAll();
        memberGroupRepository.deleteAll();
    }

    @Nested
    @DisplayName("성공 케이스")
    class SuccessCases {

        @Test
        @DisplayName("여러 개의 약속 가능 시간을 저장할 수 있다.")
        void shouldSaveMultipleAvailableTimesSuccessfully() {
            AppointmentMemberAvailableTimesRequest request = createAvailableTimesRequest();

            appointmentMemberSaveAvailableTimesService.saveAvailableTimes(savedMemberId, savedAppointmentId, request);

            List<AppointmentMemberAvailableTime> savedTimes =
                    appointmentMemberAvailableTimesRepository.findByAppointmentMember(savedAppointmentMember);

            assertThat(savedTimes).hasSize(request.appointmentMemberAvailableTimes().size());
        }

        @Test
        @DisplayName("동일한 시간 정보를 다시 저장할 경우 기존 데이터를 유지한다.")
        void shouldNotUpdateWhenSameTimesExist() {
            AppointmentMemberAvailableTimesRequest request = createAvailableTimesRequest();

            appointmentMemberSaveAvailableTimesService.saveAvailableTimes(savedMemberId, savedAppointmentId, request);

            List<AppointmentMemberAvailableTime> initialTimes =
                    appointmentMemberAvailableTimesRepository.findByAppointmentMember(savedAppointmentMember);

            appointmentMemberSaveAvailableTimesService.saveAvailableTimes(savedMemberId, savedAppointmentId, request);

            List<AppointmentMemberAvailableTime> reSavedTimes =
                    appointmentMemberAvailableTimesRepository.findByAppointmentMember(savedAppointmentMember);

            assertThat(reSavedTimes).usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(initialTimes);
        }

        @Test
        @DisplayName("새로운 시간 정보를 저장할 경우 기존 데이터를 갱신한다.")
        void shouldUpdateWhenNewTimesAdded() {
            AppointmentMemberAvailableTimesRequest initialRequest = createAvailableTimesRequest();

            appointmentMemberSaveAvailableTimesService.saveAvailableTimes(savedMemberId, savedAppointmentId, initialRequest);

            AppointmentMemberAvailableTimesRequest newRequest = new AppointmentMemberAvailableTimesRequest(List.of(
                    AppointmentMemberAvailableTimeRequest.of(
                            LocalDateTime.of(2024, 3, 15, 11, 30),
                            LocalDateTime.of(2024, 3, 15, 12, 0),
                            LocalDateTime.of(2024, 3, 15, 12, 30)
                    )
            ));

            appointmentMemberSaveAvailableTimesService.saveAvailableTimes(savedMemberId, savedAppointmentId, newRequest);

            List<AppointmentMemberAvailableTime> updatedTimes =
                    appointmentMemberAvailableTimesRepository.findByAppointmentMember(savedAppointmentMember);

            assertThat(updatedTimes).hasSize(newRequest.appointmentMemberAvailableTimes().size());
        }

        @Test
        @DisplayName("동일한 시간 정보를 저장할 경우 appointmentTimeSet이 변경되지 않는다.")
        void shouldNotChangeAppointmentTimeSetWhenSameTimesExist() {
            AppointmentMemberAvailableTimesRequest request = createAvailableTimesRequest();

            appointmentMemberSaveAvailableTimesService.saveAvailableTimes(savedMemberId, savedAppointmentId, request);

            AppointmentMember firstUpdate = appointmentMemberRepository.findByMemberIdAndAppointmentId(savedMemberId, savedAppointmentId)
                    .orElseThrow(() -> new AppointmentMemberException(AppointmentMemberErrorCode.APPOINTMENT_MEMBER_NOT_FOUND));

            boolean initialStatus = firstUpdate.isAppointmentTimeSet();

            appointmentMemberSaveAvailableTimesService.saveAvailableTimes(savedMemberId, savedAppointmentId, request);

            AppointmentMember secondUpdate = appointmentMemberRepository.findByMemberIdAndAppointmentId(savedMemberId, savedAppointmentId)
                    .orElseThrow(() -> new AppointmentMemberException(AppointmentMemberErrorCode.APPOINTMENT_MEMBER_NOT_FOUND));

            assertThat(secondUpdate.isAppointmentTimeSet()).isEqualTo(initialStatus);
        }

        @Test
        @DisplayName("가능 시간이 없는 경우 appointmentTimeSet이 true로 변경되지 않는다.")
        void shouldNotSetAppointmentTimeSetWhenNoNewTimes() {
            AppointmentMemberAvailableTimesRequest emptyRequest = new AppointmentMemberAvailableTimesRequest(List.of());

            appointmentMemberSaveAvailableTimesService.saveAvailableTimes(savedMemberId, savedAppointmentId, emptyRequest);

            AppointmentMember updatedMember = appointmentMemberRepository.findByMemberIdAndAppointmentId(savedMemberId, savedAppointmentId)
                    .orElseThrow(() -> new AppointmentMemberException(AppointmentMemberErrorCode.APPOINTMENT_MEMBER_NOT_FOUND));

            assertThat(updatedMember.isAppointmentTimeSet()).isFalse();
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class FailureCases {

        @Test
        @DisplayName("약속 멤버가 아닌 사용자가 시간을 저장하면 예외 발생")
        void shouldThrowExceptionWhenUserIsNotAppointmentMember() {
            Long invalidMemberId = 999L;
            AppointmentMemberAvailableTimesRequest request = createAvailableTimesRequest();

            assertThatThrownBy(() -> appointmentMemberSaveAvailableTimesService.saveAvailableTimes(invalidMemberId, savedAppointmentId, request))
                    .isInstanceOf(AppointmentMemberException.class)
                    .hasMessage(AppointmentMemberErrorCode.APPOINTMENT_MEMBER_NOT_FOUND.getMessage());
        }

        @Test
        @DisplayName("약속이 존재하지 않을 경우 예외 발생")
        void shouldThrowExceptionWhenAppointmentDoesNotExist() {
            Long nonExistentAppointmentId = 9999L;
            AppointmentMemberAvailableTimesRequest request = createAvailableTimesRequest();

            assertThatThrownBy(() -> appointmentMemberSaveAvailableTimesService.saveAvailableTimes(savedMemberId, nonExistentAppointmentId, request))
                    .isInstanceOf(AppointmentMemberException.class)
                    .hasMessage(AppointmentMemberErrorCode.APPOINTMENT_MEMBER_NOT_FOUND.getMessage());
        }

        @Test
        @DisplayName("멤버가 존재하지 않을 경우 예외 발생")
        void shouldThrowExceptionWhenMemberDoesNotExist() {
            Long nonExistentMemberId = 9999L;
            AppointmentMemberAvailableTimesRequest request = createAvailableTimesRequest();

            assertThatThrownBy(() -> appointmentMemberSaveAvailableTimesService.saveAvailableTimes(nonExistentMemberId, savedAppointmentId, request))
                    .isInstanceOf(AppointmentMemberException.class)
                    .hasMessage(AppointmentMemberErrorCode.APPOINTMENT_MEMBER_NOT_FOUND.getMessage());
        }
    }

    private void initializeRepositories() {
        appointmentMemberRepository = new AppointmentMemberRepositoryTest();
        appointmentMemberAvailableTimesRepository = new AppointmentMemberAvailableTimesRepositoryTest();
        memberRepository = new MemberRepositoryTest();
        appointmentRepository = new AppointmentRepositoryTest();
        groupRepository = new GroupRepositoryTest();
        memberGroupRepository = new MemberGroupRepositoryTest();
        appointmentMemberSaveAvailableTimesService = new AppointmentMemberSaveAvailableTimesServiceImpl(
                appointmentMemberRepository, appointmentMemberAvailableTimesRepository
        );
    }

    private void initializeTestData() {
        savedMemberId = createAndSaveMember("jsoonworld");

        savedGroupId = createAndSaveGroup(savedMemberId, "팀그룹", "group-image/1", "INVITE");
        saveMemberGroup(savedMemberId, savedGroupId);

        savedAppointmentId = createAndSaveAppointment(savedMemberId, "팀미팅", "중요", 60L);

        savedAppointmentMember = createAndSaveAppointmentMember(savedMemberId, savedAppointmentId);
    }

    private AppointmentMemberAvailableTimesRequest createAvailableTimesRequest() {
        return new AppointmentMemberAvailableTimesRequest(List.of(
                AppointmentMemberAvailableTimeRequest.of(
                        LocalDateTime.of(2024, 3, 15, 0, 0),
                        LocalDateTime.of(2024, 3, 15, 10, 0),
                        LocalDateTime.of(2024, 3, 15, 10, 30)
                ),
                AppointmentMemberAvailableTimeRequest.of(
                        LocalDateTime.of(2024, 3, 15, 0, 0),
                        LocalDateTime.of(2024, 3, 15, 11, 0),
                        LocalDateTime.of(2024, 3, 15, 11, 30)
                )
        ));
    }

    private Long createAndSaveMember(String name) {
        Member savedMember = memberRepository.save(
                Member.of(
                        MemberName.from(name),
                        MemberProfileImageKey.from("default-key"),
                        AuthType.GOOGLE,
                        AuthId.from("auth-id"),
                        "refresh-token"
                )
        );
        return savedMember.getId();
    }

    private Long createAndSaveGroup(Long groupHostId, String groupName, String groupImageUrl, String inviteCode) {
        return groupRepository.save(Group.of(
                groupHostId,
                GroupName.from(groupName),
                GroupProfileImageKey.from(groupImageUrl),
                inviteCode
        )).getId();
    }

    private void saveMemberGroup(Long memberId, Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new AppointmentException(AppointmentErrorCode.GROUP_NOT_FOUND));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AppointmentException(AppointmentErrorCode.MEMBER_NOT_IN_GROUP));

        memberGroupRepository.save(MemberGroup.of(member, group));
    }

    private Long createAndSaveAppointment(Long memberId, String name, String category, Long duration) {
        Group group = memberGroupRepository.findByMemberId(memberId).stream()
                .findFirst()
                .map(MemberGroup::getGroup)
                .orElseThrow(() -> new AppointmentException(AppointmentErrorCode.GROUP_NOT_FOUND));

        Appointment savedAppointment = appointmentRepository.save(
                Appointment.of(
                        group,
                        memberId,
                        name,
                        duration,
                        category,
                        AppointmentStatus.PROGRESS
                )
        );
        return savedAppointment.getId();
    }

    private AppointmentMember createAndSaveAppointmentMember(Long memberId, Long appointmentId) {
        return appointmentMemberRepository.save(AppointmentMember.of(
                AppointmentAvailability.AVAILABLE,
                appointmentRepository.findById(appointmentId).orElseThrow(),
                memberRepository.findById(memberId).orElseThrow()
        ));
    }
}
