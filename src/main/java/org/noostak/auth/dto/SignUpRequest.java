package org.noostak.auth.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class SignUpRequest {
    String memberName;
    MultipartFile memberProfileImage;
    String authType;
}
