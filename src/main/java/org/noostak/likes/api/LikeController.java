package org.noostak.likes.api;


import lombok.RequiredArgsConstructor;
import org.noostak.likes.application.LikeService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/{appointmentId}/appointment-options/{appointmentOptionId}/like")
    public void increase(
                        @RequestAttribute("memberId") Long memberId,
                        @PathVariable long appointmentId,
                        @PathVariable long appointmentOptionId){
        likeService.increase(memberId, appointmentId, appointmentOptionId);
    }

}
