package org.noostak.appointmentmember.api;

import lombok.RequiredArgsConstructor;
import org.noostak.appointmentmember.application.AppointmentSaveAvailableTimesService;
import org.noostak.appointmentmember.dto.request.AvailableTimesRequest;
import org.noostak.global.success.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.noostak.appointmentmember.common.success.AppointmentMemberSuccessCode.SUCCESS_SAVE_AVAILABLE_TIMES;

@RestController
@RequestMapping("/api/v1/appointment-members")
@RequiredArgsConstructor
public class AppointmentMemberController {

    private final AppointmentSaveAvailableTimesService appointmentSaveAvailableTimesService;

    @PostMapping("/{appointmentId}/timetable")
    public ResponseEntity<SuccessResponse> saveAvailableTimes(
            // @AuthenticationPrincipal Long memberId
            @PathVariable(name = "appointmentId") Long appointmentId,
            @RequestBody AvailableTimesRequest request
    ) {
        Long memberId = 2L;
        appointmentSaveAvailableTimesService.saveAvailableTimes(memberId, appointmentId, request);
        return ResponseEntity.ok(SuccessResponse.of(SUCCESS_SAVE_AVAILABLE_TIMES));
    }
}
