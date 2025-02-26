package org.noostak.member.api;


import lombok.RequiredArgsConstructor;
import org.noostak.global.success.SuccessResponse;
import org.noostak.member.application.MemberService;
import org.noostak.member.common.success.MemberSuccessCode;
import org.noostak.member.dto.GetProfileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<SuccessResponse> getProfile(@RequestAttribute Long memberId){
        GetProfileResponse response = memberService.fetchMember(memberId);
        return ResponseEntity.ok(SuccessResponse.of(MemberSuccessCode.MEMBER_FETCH_COMPLETE,response));
    }
}
