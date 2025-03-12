package org.noostak.global.config.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.noostak.appointmentmember.dto.request.AppointmentMemberAvailableTimesRequest;
import org.noostak.appointmentmember.dto.response.AppointmentMembersAvailableTimesResponse;
import org.noostak.global.success.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "AppointmentMember", description = "약속 멤버 관련 API")
public interface AppointmentMemberSwagger {

    @Operation(summary = "약속 멤버 가능 시간 저장", description = "약속 멤버가 가능한 시간을 저장하는 API")
    ResponseEntity<SuccessResponse> saveAvailableTimes(
            @Parameter(description = "멤버 ID", example = "1")
            Long memberId,

            @Parameter(description = "약속 ID", example = "100")
            Long appointmentId,

            @RequestBody AppointmentMemberAvailableTimesRequest request
    );

    @Operation(summary = "약속 멤버 가능 시간 조회", description = "약속 멤버들의 가능한 시간을 조회하는 API")
    ResponseEntity<SuccessResponse<AppointmentMembersAvailableTimesResponse>> retrieveAvailableTimes(
            @Parameter(description = "멤버 ID", example = "1")
            Long memberId,

            @Parameter(description = "약속 ID", example = "100")
            Long appointmentId
    );
}
