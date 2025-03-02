package org.noostak.member.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class UpdateProfileRequest {
    String memberName;
    MultipartFile memberProfileImage;
}
