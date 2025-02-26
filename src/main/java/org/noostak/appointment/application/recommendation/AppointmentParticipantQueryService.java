package org.noostak.appointment.application.recommendation;

import lombok.RequiredArgsConstructor;
import org.noostak.appointmentmember.domain.AppointmentMemberAvailableTimesRepository;
import org.noostak.member.domain.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AppointmentParticipantQueryService {
    private final AppointmentMemberAvailableTimesRepository availableTimesRepository;

    public Map<Long, String> findParticipantNamesByAppointmentId(Long appointmentId) {
        return availableTimesRepository.findByAppointmentMember_AppointmentId(appointmentId).stream()
                .map(memberTime -> memberTime.getAppointmentMember().getMember())
                .collect(Collectors.toMap(
                        Member::getId,
                        member -> member.getName().value(),
                        (existing, duplicate) -> existing
                ));
    }
}
