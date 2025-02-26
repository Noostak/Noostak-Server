package org.noostak.appointment.application.recommendation;

import lombok.RequiredArgsConstructor;
import org.noostak.appointment.domain.Appointment;
import org.noostak.appointment.util.TimeSlot;
import org.noostak.appointment.dto.response.AppointmentOptionAvailabilityResponse;
import org.noostak.appointment.dto.response.AppointmentPriorityGroupResponse;
import org.noostak.appointment.util.AppointmentAvailabilityMatcher;
import org.noostak.appointment.util.AppointmentOptionSorter;
import org.noostak.appointmentmember.domain.AppointmentMemberAvailableTime;
import org.noostak.appointmentoption.domain.AppointmentOption;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AppointmentRecommendationFacade {

    private final AppointmentHostSelectedTimeQueryService appointmentHostSelectedTimeQueryService;
    private final AppointmentMemberAvailabilityQueryService appointmentMemberAvailabilityQueryService;
    private final AppointmentOptionCommandService appointmentOptionCommandService;
    private final AppointmentParticipantQueryService appointmentParticipantQueryService;
    private final AppointmentOptionPriorityService optionPriorityService;

    public List<AppointmentPriorityGroupResponse> getRecommendedOptions(Long memberId, Long appointmentId, Appointment appointment) {
        List<TimeSlot> timeSlots = appointmentHostSelectedTimeQueryService.splitHostSelectedTimeSlots(appointmentId, 60L);

        Map<Long, List<AppointmentMemberAvailableTime>> memberAvailability = appointmentMemberAvailabilityQueryService.findAvailableTimeSlotsByAppointmentId(appointmentId);

        List<AppointmentOptionAvailabilityResponse> optionDTOs = AppointmentAvailabilityMatcher.matchAvailability(timeSlots, memberAvailability);

        List<AppointmentOption> savedOptions = appointmentOptionCommandService.saveOptions(appointment, optionDTOs);

        List<AppointmentOption> sortedOptions = AppointmentOptionSorter.sortAndFilterOptions(savedOptions, memberAvailability);

        Map<Long, String> memberNames = appointmentParticipantQueryService.findParticipantNamesByAppointmentId(appointmentId);

        return optionPriorityService.determineOptionPriority(sortedOptions, memberId, memberAvailability, memberNames);
    }
}
