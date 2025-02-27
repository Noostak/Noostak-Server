package org.noostak.appointment.application.recommendation.impl;

import lombok.RequiredArgsConstructor;
import org.noostak.appointment.application.recommendation.AppointmentHostSelectedTimeQueryService;
import org.noostak.appointment.common.exception.AppointmentErrorCode;
import org.noostak.appointment.common.exception.AppointmentException;
import org.noostak.appointment.domain.AppointmentHostSelectionTime;
import org.noostak.appointment.domain.AppointmentHostSelectionTimeRepository;
import org.noostak.appointment.util.TimeSlot;
import org.noostak.appointment.util.AppointmentTimeSplitter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AppointmentHostSelectedTimeQueryServiceImpl implements AppointmentHostSelectedTimeQueryService {
    private final AppointmentHostSelectionTimeRepository hostSelectionTimeRepository;

    @Override
    public List<TimeSlot> splitHostSelectedTimeSlots(Long appointmentId, Long durationInMinutes) {
        List<AppointmentHostSelectionTime> selectedTimes = findSelectedTimes(appointmentId);
        return selectedTimes.stream()
                .flatMap(selectionTime -> splitTimeSlotsFromSelectionTime(selectionTime, durationInMinutes).stream())
                .toList();
    }

    private List<AppointmentHostSelectionTime> findSelectedTimes(Long appointmentId) {
        List<AppointmentHostSelectionTime> selectedTimes = hostSelectionTimeRepository.findByAppointmentId(appointmentId);
        if (selectedTimes.isEmpty()) {
            throw new AppointmentException(AppointmentErrorCode.HOST_SELECTION_TIME_NOT_FOUND);
        }
        return selectedTimes;
    }

    private List<TimeSlot> splitTimeSlotsFromSelectionTime(AppointmentHostSelectionTime selectionTime, Long durationInMinutes) {
        LocalDateTime date = selectionTime.getStartTime().toLocalDate().atStartOfDay();
        return AppointmentTimeSplitter.splitTimeSlots(date, selectionTime.getStartTime(), selectionTime.getEndTime(), durationInMinutes.intValue());
    }
}
