package org.noostak.global.config.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.noostak.global.success.SuccessResponse;
import org.noostak.member.dto.UpdateProfileRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestAttribute;

@Tag(name = "Member", description = "멤버 프로필 관련 API")
public interface MemberSwagger {

    @GetMapping
    @Operation(summary = "회원 프로필 조회", description = "사용자의 프로필 정보를 조회합니다.")
    ResponseEntity<SuccessResponse> getProfile(
            @Parameter(description = "사용자 ID", required = true)
            @RequestAttribute Long memberId);

    @PatchMapping
    @Operation(summary = "회원 프로필 수정", description = "사용자의 프로필 정보를 수정합니다.")
    ResponseEntity<SuccessResponse> updateProfile(
            @Parameter(description = "사용자 ID", required = true)
            @RequestAttribute Long memberId,
            @Parameter(description = "수정할 프로필 정보", required = true)
            @ModelAttribute UpdateProfileRequest request);
}
