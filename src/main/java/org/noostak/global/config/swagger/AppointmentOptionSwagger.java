package org.noostak.global.config;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.noostak.global.success.SuccessResponse;
import org.springframework.http.ResponseEntity;

@Tag(name = "AppointmentOption", description = "약속 옵션 관련 API")
public interface AppointmentOptionSwagger {

    @Operation(summary = "약속 옵션 확정", description = "약속 옵션을 확정하는 API")
    ResponseEntity<SuccessResponse> confirmAppointment(
            @Parameter(description = "약속 옵션 ID", example = "200")
            Long appointmentOptionId
    );
}
