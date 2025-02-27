package org.noostak.likes.api;


import lombok.RequiredArgsConstructor;
import org.noostak.global.success.SuccessResponse;
import org.noostak.likes.application.LikeService;
import org.noostak.likes.common.success.LikesSuccessCode;
import org.noostak.likes.dto.DecreaseResponse;
import org.noostak.likes.dto.IncreaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/{appointmentId}/appointment-options/{appointmentOptionId}/like")
    public ResponseEntity<?> increase(
            @RequestAttribute("memberId") Long memberId,
            @PathVariable long appointmentId,
            @PathVariable long appointmentOptionId) {

        IncreaseResponse response = likeService.increase(memberId, appointmentId, appointmentOptionId);

        return ResponseEntity.ok(SuccessResponse.of(LikesSuccessCode.LIKE_CREATED, response));
    }

    @DeleteMapping("/{appointmentId}/appointment-options/{appointmentOptionId}/like")
    public ResponseEntity<?> decrease(
            @RequestAttribute("memberId") Long memberId,
            @PathVariable long appointmentId,
            @PathVariable long appointmentOptionId) {

        DecreaseResponse response = likeService.decrease(memberId, appointmentId, appointmentOptionId);

        return ResponseEntity.ok(SuccessResponse.of(LikesSuccessCode.LIKE_DELETED, response));
    }

}
