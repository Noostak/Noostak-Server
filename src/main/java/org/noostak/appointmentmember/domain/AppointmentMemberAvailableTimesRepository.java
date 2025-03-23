package org.noostak.appointmentmember.domain;

import org.noostak.appointmentoption.domain.AppointmentOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentMemberAvailableTimesRepository extends JpaRepository<AppointmentMemberAvailableTime, Long> {

    List<AppointmentMemberAvailableTime> findByAppointmentMember(AppointmentMember appointmentMember);

    List<AppointmentMemberAvailableTime> findByAppointmentMember_AppointmentId(Long appointmentId);

    void deleteByAppointmentMember(AppointmentMember appointmentMember);

    boolean existsByAppointmentMemberAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual
            (AppointmentMember appointmentMember, LocalDateTime givenStartTime, LocalDateTime givenEndTime);

    default boolean hasMemberAvailableTime
            (AppointmentMember appointmentMember, AppointmentOption option){
        return existsByAppointmentMemberAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual
                (appointmentMember,option.getStartTime(),option.getEndTime());
    }
}
