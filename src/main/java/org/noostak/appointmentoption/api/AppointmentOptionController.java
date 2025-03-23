package org.noostak.appointmentoption.api;

import lombok.RequiredArgsConstructor;
import org.noostak.appointmentoption.application.AppointmentOptionService;
import org.noostak.global.success.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.noostak.appointment.common.success.AppointmentSuccessCode.APPOINTMENT_CONFIRMED;

@RestController
@RequestMapping("/api/v1/appointment-options")
@RequiredArgsConstructor
public class AppointmentOptionController {

    private final AppointmentOptionService appointmentService;

    @PostMapping("/{appointmentOptionId}/confirm")
    public ResponseEntity<SuccessResponse> confirmAppointment(
            @PathVariable Long appointmentOptionId
    ) {
        appointmentService.confirmAppointment(appointmentOptionId);
        return ResponseEntity.ok(SuccessResponse.of(APPOINTMENT_CONFIRMED));
    }
}