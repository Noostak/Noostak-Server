package org.noostak.appointmentoption.application.info;


import org.noostak.appointmentoption.dto.response.info.AppointmentOptionInfoResponse;

public interface AppointmentOptionInfoService {
    AppointmentOptionInfoResponse getAppointmentOptionInfo(Long memberId, Long appointmentOptionId);
}
