package org.noostak.appointmentmember.application;

import org.noostak.appointmentmember.dto.request.AvailableTimesRequest;

public interface AppointmentSaveAvailableTimesService {
    void saveAvailableTimes(Long memberId, Long appointmentId, AvailableTimesRequest request);
}
