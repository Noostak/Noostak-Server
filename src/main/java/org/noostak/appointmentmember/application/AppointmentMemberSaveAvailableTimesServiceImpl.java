package org.noostak.appointmentmember.application;

import lombok.RequiredArgsConstructor;
import org.noostak.appointmentmember.common.exception.AppointmentMemberErrorCode;
import org.noostak.appointmentmember.common.exception.AppointmentMemberException;
import org.noostak.appointmentmember.domain.AppointmentMemberAvailableTime;
import org.noostak.appointmentmember.domain.AppointmentMemberAvailableTimesRepository;
import org.noostak.appointmentmember.dto.request.AppointmentMemberAvailableTimesRequest;
import org.noostak.appointmentmember.domain.AppointmentMember;
import org.noostak.appointmentmember.domain.AppointmentMemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AppointmentMemberSaveAvailableTimesServiceImpl implements AppointmentMemberSaveAvailableTimesService {

    private final AppointmentMemberRepository appointmentMemberRepository;
    private final AppointmentMemberAvailableTimesRepository appointmentMemberAvailableTimesRepository;

    @Override
    @Transactional
    public void saveAvailableTimes(Long memberId, Long appointmentId, AppointmentMemberAvailableTimesRequest request) {
        AppointmentMember appointmentMember = findAppointmentMember(memberId, appointmentId);
        List<AppointmentMemberAvailableTime> newTimes = createNewAvailableTimes(appointmentMember, request);

        refreshAvailableTimes(appointmentMember, newTimes);
    }

    private AppointmentMember findAppointmentMember(Long memberId, Long appointmentId) {
        return appointmentMemberRepository.findByMemberIdAndAppointmentId(memberId, appointmentId)
                .orElseThrow(() -> new AppointmentMemberException(AppointmentMemberErrorCode.APPOINTMENT_MEMBER_NOT_FOUND));
    }

    private List<AppointmentMemberAvailableTime> createNewAvailableTimes(AppointmentMember appointmentMember, AppointmentMemberAvailableTimesRequest request) {
        return request.appointmentMemberAvailableTimes().stream()
                .map(time -> AppointmentMemberAvailableTime.of(appointmentMember, time.date(), time.startTime(), time.endTime()))
                .collect(Collectors.toList());
    }

    private void refreshAvailableTimes(AppointmentMember appointmentMember, List<AppointmentMemberAvailableTime> newTimes) {
        if (!isTimeUpdateRequired(appointmentMember, newTimes)) {
            return;
        }

        updateAvailableTimes(appointmentMember, newTimes);
        markAppointmentTimeIfNecessary(appointmentMember, newTimes);
    }

    private boolean isTimeUpdateRequired(AppointmentMember appointmentMember, List<AppointmentMemberAvailableTime> newTimes) {
        List<AppointmentMemberAvailableTime> existingTimes = appointmentMemberAvailableTimesRepository.findByAppointmentMember(appointmentMember);
        return !Set.copyOf(existingTimes).equals(Set.copyOf(newTimes));
    }

    private void updateAvailableTimes(AppointmentMember appointmentMember, List<AppointmentMemberAvailableTime> newTimes) {
        appointmentMemberAvailableTimesRepository.deleteByAppointmentMember(appointmentMember);
        appointmentMemberAvailableTimesRepository.saveAll(newTimes);
    }

    private void markAppointmentTimeIfNecessary(AppointmentMember appointmentMember, List<AppointmentMemberAvailableTime> newTimes) {
        appointmentMember.updateAvailableTimes(newTimes);
    }
}
