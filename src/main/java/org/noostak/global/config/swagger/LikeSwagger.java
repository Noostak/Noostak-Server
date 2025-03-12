package org.noostak.global.config.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Like", description = "좋아요 관련 API")
public interface LikeSwagger {

    @PostMapping("/{appointmentId}/appointment-options/{appointmentOptionId}/like")
    @Operation(summary = "좋아요 증가", description = "사용자가 특정 약속 옵션에 좋아요를 추가합니다.")
    ResponseEntity<?> increase(
            @Parameter(description = "사용자 ID", required = true)
            @RequestAttribute Long memberId,
            @Parameter(description = "약속 ID", required = true)
            @PathVariable long appointmentId,
            @Parameter(description = "약속 옵션 ID", required = true)
            @PathVariable long appointmentOptionId);

    @DeleteMapping("/{appointmentId}/appointment-options/{appointmentOptionId}/like")
    @Operation(summary = "좋아요 감소", description = "사용자가 특정 약속 옵션의 좋아요를 취소합니다.")
    ResponseEntity<?> decrease(
            @Parameter(description = "사용자 ID", required = true)
            @RequestAttribute Long memberId,
            @Parameter(description = "약속 ID", required = true)
            @PathVariable long appointmentId,
            @Parameter(description = "약속 옵션 ID", required = true)
            @PathVariable long appointmentOptionId);
}