package org.noostak.appointment.api;

import lombok.RequiredArgsConstructor;
import org.noostak.appointment.application.AppointmentService;
import org.noostak.appointment.dto.request.AppointmentCreateRequest;
import org.noostak.global.success.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.noostak.appointment.common.success.AppointmentSuccessCode.APPOINTMENT_CREATED;

@RestController
@RequestMapping("/api/v1/groups/{groupId}/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<SuccessResponse> createAppointment(
            // @AuthenticationPrincipal Long memberId,
            @PathVariable(name = "groupId") Long groupId,
            @RequestBody AppointmentCreateRequest request
    ) {
        Long memberId = 1L;
        appointmentService.createAppointment(memberId, groupId, request);
        return ResponseEntity.ok(SuccessResponse.of(APPOINTMENT_CREATED));
    }
}
