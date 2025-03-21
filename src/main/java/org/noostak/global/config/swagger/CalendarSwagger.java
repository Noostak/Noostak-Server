package org.noostak.global.config.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.noostak.global.success.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Calendar", description = "캘린더 관련 API")
public interface CalendarSwagger {

    @Operation(summary = "캘린더 조회", description = "특정 그룹의 캘린더 정보를 조회하는 API")
    @GetMapping("/api/v1/groups/{groupId}/appointments/calendar")
    ResponseEntity<SuccessResponse> getCalendar(
            @Parameter(description = "멤버 ID", example = "1")
            Long memberId,

            @Parameter(description = "그룹 ID", example = "10")
            @PathVariable Long groupId,

            @Parameter(description = "연도", example = "2025")
            @RequestParam int year,

            @Parameter(description = "월", example = "3")
            @RequestParam int month
    );
}
