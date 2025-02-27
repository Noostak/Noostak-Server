package org.noostak.appointment.api;

import lombok.RequiredArgsConstructor;
import org.noostak.appointment.application.AppointmentService;
import org.noostak.appointment.dto.request.AppointmentCreateRequest;
import org.noostak.appointment.dto.response.recommendation.AppointmentRecommendedOptionsResponse;
import org.noostak.global.success.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.noostak.appointment.common.success.AppointmentSuccessCode.APPOINTMENT_CREATED;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    // TODO : GROUP 으로 이동
    @PostMapping("/groups/{groupId}/appointments")
    public ResponseEntity<SuccessResponse> createAppointment(
            // @AuthenticationPrincipal Long memberId,
            @PathVariable(name = "groupId") Long groupId,
            @RequestBody AppointmentCreateRequest request
    ) {
        Long memberId = 1L;
        appointmentService.createAppointment(memberId, groupId, request);
        return ResponseEntity.ok(SuccessResponse.of(APPOINTMENT_CREATED));
    }

    @GetMapping("/appointments/{appointmentId}/recommended-options")
    public ResponseEntity<SuccessResponse<AppointmentRecommendedOptionsResponse>> getRecommendedAppointmentOptions(
            @PathVariable(name = "appointmentId") Long appointmentId
    ) {
        Long memberId = 1L;
        AppointmentRecommendedOptionsResponse response = appointmentService.getRecommendedAppointmentOptions(memberId, appointmentId);
        return ResponseEntity.ok(SuccessResponse.of(APPOINTMENT_CREATED, response));
    }
}
