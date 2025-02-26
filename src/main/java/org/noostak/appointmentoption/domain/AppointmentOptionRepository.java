package org.noostak.appointmentoption.domain;

import org.noostak.appointmentoption.common.exception.AppointmentOptionErrorCode;
import org.noostak.appointmentoption.common.exception.AppointmentOptionException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentOptionRepository extends JpaRepository<AppointmentOption, Long> {

    default AppointmentOption getById(Long optionId){
        return findById(optionId)
                .orElseThrow(()-> new AppointmentOptionException(AppointmentOptionErrorCode.APPOINTMENT_OPTION_NOT_FOUND));
    }
}
