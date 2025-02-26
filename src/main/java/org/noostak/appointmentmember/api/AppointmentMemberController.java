package org.noostak.appointmentmember.api;

import lombok.RequiredArgsConstructor;
import org.noostak.appointmentmember.application.AppointmentMemberRetrieveAvailableTimesService;
import org.noostak.appointmentmember.application.AppointmentMemberSaveAvailableTimesService;
import org.noostak.appointmentmember.dto.request.AppointmentMemberAvailableTimesRequest;
import org.noostak.appointmentmember.dto.response.AppointmentMembersAvailableTimesResponse;
import org.noostak.global.success.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.noostak.appointmentmember.common.success.AppointmentMemberSuccessCode.APPOINTMENT_MEMBER_AVAILABLE_TIMES_RETRIEVED;
import static org.noostak.appointmentmember.common.success.AppointmentMemberSuccessCode.SUCCESS_SAVE_AVAILABLE_TIMES;

@RestController
@RequestMapping("/api/v1/appointment-members")
@RequiredArgsConstructor
public class AppointmentMemberController {

    private final AppointmentMemberSaveAvailableTimesService appointmentMemberSaveAvailableTimesService;
    private final AppointmentMemberRetrieveAvailableTimesService appointmentMemberRetrieveAvailableTimesService;


    @PostMapping("/{appointmentId}/timetable")
    public ResponseEntity<SuccessResponse> saveAvailableTimes(
            // @AuthenticationPrincipal Long memberId
            @PathVariable(name = "appointmentId") Long appointmentId,
            @RequestBody AppointmentMemberAvailableTimesRequest request
    ) {
        Long memberId = 2L;
        appointmentMemberSaveAvailableTimesService.saveAvailableTimes(memberId, appointmentId, request);
        return ResponseEntity.ok(SuccessResponse.of(SUCCESS_SAVE_AVAILABLE_TIMES));
    }

    @GetMapping("/{appointmentId}/timetable")
    public ResponseEntity<SuccessResponse<AppointmentMembersAvailableTimesResponse>> retrieveAvailableTimes(
            // @AuthenticationPrincipal Long memberId
            @PathVariable(name = "appointmentId") Long appointmentId
    ) {
        Long memberId = 3L;
        AppointmentMembersAvailableTimesResponse response = appointmentMemberRetrieveAvailableTimesService.retrieveAvailableTimes(memberId, appointmentId);
        return ResponseEntity.ok(SuccessResponse.of(APPOINTMENT_MEMBER_AVAILABLE_TIMES_RETRIEVED, response));
    }
}
