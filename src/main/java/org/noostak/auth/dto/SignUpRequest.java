package org.noostak.auth.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
@ToString
public class SignUpRequest {
    String memberName;
    MultipartFile memberProfileImage;
    String authId;
    String authType;

    public String getAuthType(){
        return authType.toUpperCase();
    }
}
