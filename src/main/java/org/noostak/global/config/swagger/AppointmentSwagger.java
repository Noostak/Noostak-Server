package org.noostak.global.config.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.noostak.appointment.dto.request.AppointmentCreateRequest;
import org.noostak.appointment.dto.response.recommendation.AppointmentRecommendedOptionsResponse;
import org.noostak.global.success.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Appointment", description = "약속 관련 API")
@RequestMapping("/api/appointments")
public interface AppointmentSwagger {

    @Operation(summary = "약속 생성", description = "새로운 약속을 생성하는 API")
    ResponseEntity<SuccessResponse> createAppointment(
            @Parameter(description = "멤버 ID", example = "1")
            Long memberId,

            @Parameter(description = "그룹 ID", example = "10")
            Long groupId,

            @RequestBody AppointmentCreateRequest request
    );

    @Operation(summary = "추천 약속 옵션 조회", description = "추천된 약속 옵션을 조회하는 API")
    ResponseEntity<SuccessResponse<AppointmentRecommendedOptionsResponse>> getRecommendedAppointmentOptions(
            @Parameter(description = "멤버 ID", example = "1")
            Long memberId,

            @Parameter(description = "약속 ID", example = "100")
            Long appointmentId
    );
}
