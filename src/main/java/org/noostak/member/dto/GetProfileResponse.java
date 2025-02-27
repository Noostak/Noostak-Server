package org.noostak.member.dto;


import lombok.Getter;

@Getter
public class GetProfileResponse {
    String membername;
    String memberProfileImage;

    private GetProfileResponse(String membername, String memberProfileImage) {

        this.membername = membername;
        this.memberProfileImage = memberProfileImage;
    }

    public static GetProfileResponse of(String membername, String memberProfileImage){
        return new GetProfileResponse(membername,memberProfileImage);
    }
}
