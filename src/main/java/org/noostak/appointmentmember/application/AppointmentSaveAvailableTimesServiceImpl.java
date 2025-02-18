package org.noostak.appointmentmember.application;

import lombok.RequiredArgsConstructor;
import org.noostak.appointmentmember.common.exception.AppointmentMemberErrorCode;
import org.noostak.appointmentmember.common.exception.AppointmentMemberException;
import org.noostak.appointmentmember.domain.AppointmentMemberAvailableTimes;
import org.noostak.appointmentmember.domain.repository.AppointmentMemberAvailableTimesRepository;
import org.noostak.appointmentmember.dto.request.AvailableTimesRequest;
import org.noostak.appointmentmember.domain.AppointmentMember;
import org.noostak.appointmentmember.domain.repository.AppointmentMemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AppointmentSaveAvailableTimesServiceImpl implements AppointmentSaveAvailableTimesService {

    private final AppointmentMemberRepository appointmentMemberRepository;
    private final AppointmentMemberAvailableTimesRepository appointmentMemberAvailableTimesRepository;

    @Override
    @Transactional
    public void saveAvailableTimes(Long memberId, Long appointmentId, AvailableTimesRequest request) {
        AppointmentMember appointmentMember = findAppointmentMember(memberId, appointmentId);
        List<AppointmentMemberAvailableTimes> newTimes = createNewAvailableTimes(appointmentMember, request);

        refreshAvailableTimes(appointmentMember, newTimes);
    }

    private AppointmentMember findAppointmentMember(Long memberId, Long appointmentId) {
        return appointmentMemberRepository.findByMemberIdAndAppointmentId(memberId, appointmentId)
                .orElseThrow(() -> new AppointmentMemberException(AppointmentMemberErrorCode.APPOINTMENT_MEMBER_NOT_FOUND));
    }

    private List<AppointmentMemberAvailableTimes> createNewAvailableTimes(AppointmentMember appointmentMember, AvailableTimesRequest request) {
        return request.appointmentMemberAvailableTimes().stream()
                .map(time -> AppointmentMemberAvailableTimes.of(appointmentMember, time.date(), time.startTime(), time.endTime()))
                .collect(Collectors.toList());
    }

    private void refreshAvailableTimes(AppointmentMember appointmentMember, List<AppointmentMemberAvailableTimes> newTimes) {
        List<AppointmentMemberAvailableTimes> existingTimes = appointmentMemberAvailableTimesRepository.findByAppointmentMember(appointmentMember);

        Set<AppointmentMemberAvailableTimes> existingSet = Set.copyOf(existingTimes);
        Set<AppointmentMemberAvailableTimes> newSet = Set.copyOf(newTimes);

        if (!existingSet.equals(newSet)) {
            appointmentMemberAvailableTimesRepository.deleteByAppointmentMember(appointmentMember);
            appointmentMemberAvailableTimesRepository.saveAll(newTimes);
        }
    }
}
