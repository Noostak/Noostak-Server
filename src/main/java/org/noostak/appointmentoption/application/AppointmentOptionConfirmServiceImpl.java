package org.noostak.appointmentoption.application;

import lombok.RequiredArgsConstructor;
import org.noostak.appointment.domain.Appointment;
import org.noostak.appointmentmember.domain.AppointmentMember;
import org.noostak.appointmentmember.domain.AppointmentMemberAvailableTimesRepository;
import org.noostak.appointmentmember.domain.AppointmentMemberRepository;
import org.noostak.appointmentmember.domain.vo.AppointmentAvailability;
import org.noostak.appointmentoption.common.exception.AppointmentOptionErrorCode;
import org.noostak.appointmentoption.common.exception.AppointmentOptionException;
import org.noostak.appointmentoption.domain.AppointmentOption;
import org.noostak.appointmentoption.domain.AppointmentOptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AppointmentOptionConfirmServiceImpl implements AppointmentOptionConfirmService {
    private final AppointmentOptionRepository appointmentOptionRepository;
    private final AppointmentMemberRepository appointmentMemberRepository;
    private final AppointmentMemberAvailableTimesRepository memberAvailableTimesRepository;

    @Override
    @Transactional
    public void confirmAppointment(Long appointmentOptionId) {
        AppointmentOption confirmedAppointmentOption = confirmAppointmentOption(appointmentOptionId);
        Appointment appointment = confirmedAppointmentOption.getAppointment();

        appointment.confirm();

        updateMembersAvailableStatusInAppointment(appointment,confirmedAppointmentOption);
    }

    private void updateMembersAvailableStatusInAppointment(Appointment appointment, AppointmentOption option) {
        List<AppointmentMember> appointmentMembers =
                appointmentMemberRepository.findByAppointmentId(appointment.getId());

        appointmentMembers.forEach(appointmentMember -> {
            if(isAvailable(option, appointmentMember)){
                appointmentMember.setAppointmentAvailability(AppointmentAvailability.AVAILABLE);
            }else {
                appointmentMember.setAppointmentAvailability(AppointmentAvailability.UNAVAILABLE);
            }
        });
    }

    private boolean isAvailable(AppointmentOption option, AppointmentMember appointmentMember) {
        return memberAvailableTimesRepository.hasMemberAvailableTime(appointmentMember, option);
    }

    private AppointmentOption confirmAppointmentOption(Long appointmentOptionId){
        AppointmentOption appointmentOption = appointmentOptionRepository.findById(appointmentOptionId)
                .orElseThrow(() -> new AppointmentOptionException(AppointmentOptionErrorCode.APPOINTMENT_OPTION_NOT_FOUND));

        appointmentOption.confirm();

        return appointmentOption;
    }
}
