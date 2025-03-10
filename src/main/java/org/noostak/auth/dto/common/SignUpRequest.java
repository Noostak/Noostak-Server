package org.noostak.auth.dto.common;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class SignUpRequest {
    private String memberName;
    private MultipartFile memberProfileImage;
    private String authType;

    public String getAuthType(){
        return authType.toUpperCase();
    }
}
