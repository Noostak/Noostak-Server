package org.noostak.appointmentoption.domain;

import org.noostak.likes.common.exception.LikesErrorCode;
import org.noostak.likes.common.exception.LikesException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentOptionRepository extends JpaRepository<AppointmentOption, Long>, AppointmentOptionRepositoryCustom {

    default AppointmentOption getByAppointmentOptionId(Long appointmentId){
        return findById(appointmentId)
                .orElseThrow(() -> new LikesException(LikesErrorCode.OPTION_NOT_FOUND));
    }
}
