package org.noostak.member.api;


import lombok.RequiredArgsConstructor;
import org.noostak.global.success.SuccessResponse;
import org.noostak.member.application.MemberService;
import org.noostak.member.common.success.MemberSuccessCode;
import org.noostak.member.dto.GetProfileResponse;
import org.noostak.member.dto.UpdateProfileRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping
    public ResponseEntity<SuccessResponse> updateProfile(
            @RequestAttribute Long memberId,
            @ModelAttribute UpdateProfileRequest request
            ){
        memberService.updateMember(memberId, request.getMemberName(), request.getMemberProfileImage());
        return ResponseEntity.ok(SuccessResponse.of(MemberSuccessCode.MEMBER_UPDATE_COMPLETE));
    }

    @DeleteMapping
    public ResponseEntity<SuccessResponse> deleteMember(
            @RequestAttribute Long memberId
    ){
        memberService.deleteMember(memberId);
        return ResponseEntity.ok(SuccessResponse.of(MemberSuccessCode.MEMBER_DELETE_COMPLETE));
    }
}
