package org.noostak.auth.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class SignUpResponse {
    private String accessToken;
    private String refreshToken;
    private Long memberId;
    private String authType;

    private SignUpResponse(String accessToken, String refreshToken, Long memberId, String authType) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.memberId = memberId;
        this.authType = authType;
    }

    public static SignUpResponse of(String accessToken, String refreshToken, Long memberId, String authType){
        return new SignUpResponse(accessToken,refreshToken,memberId,authType);
    }
}
