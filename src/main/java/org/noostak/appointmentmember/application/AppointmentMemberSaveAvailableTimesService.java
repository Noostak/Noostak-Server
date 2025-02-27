package org.noostak.appointmentmember.application;

import org.noostak.appointmentmember.dto.request.AppointmentMemberAvailableTimesRequest;

public interface AppointmentMemberSaveAvailableTimesService {
    void saveAvailableTimes(Long memberId, Long appointmentId, AppointmentMemberAvailableTimesRequest request);
}
