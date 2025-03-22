package org.noostak.appointment.application.recommendation.impl;

import lombok.RequiredArgsConstructor;
import org.noostak.appointment.application.recommendation.AppointmentMemberAvailabilityQueryService;
import org.noostak.appointmentmember.domain.AppointmentMemberAvailableTime;
import org.noostak.appointmentmember.domain.AppointmentMemberAvailableTimesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AppointmentMemberAvailabilityQueryServiceImpl implements AppointmentMemberAvailabilityQueryService {
    private final AppointmentMemberAvailableTimesRepository availableTimesRepository;

    @Override
    public Map<Long, List<AppointmentMemberAvailableTime>> findAvailableTimeSlotsByAppointmentId(Long appointmentId) {
        List<AppointmentMemberAvailableTime> availableTimes =
                availableTimesRepository.findByAppointmentMember_AppointmentId(appointmentId);

        return availableTimes.stream()
                .collect(Collectors.groupingBy(time -> time.getAppointmentMember().getMember().getId()));
    }
}
