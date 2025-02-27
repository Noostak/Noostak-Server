package org.noostak.member.dto;


import lombok.Getter;

@Getter
public class GetProfileResponse {
    String memberName;
    String memberProfileImage;

    private GetProfileResponse(String memberName, String memberProfileImage) {

        this.memberName = memberName;
        this.memberProfileImage = memberProfileImage;
    }

    public static GetProfileResponse of(String memberName, String memberProfileImage){
        return new GetProfileResponse(memberName,memberProfileImage);
    }
}
