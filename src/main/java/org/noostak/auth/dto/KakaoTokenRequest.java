package org.noostak.auth.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoTokenRequest {
    @Value("${oauth-property.kakao.grant_type}")
    private String grantType;

    @Value("${oauth-property.kakao.client_id}")
    private String clientId;

    @Value("${oauth-property.kakao.redirect_uri}")
    private String redirectUri;

    private String code;

    private KakaoTokenRequest(String code){
        this.code=code;
    }

    public static KakaoTokenRequest of(String code){
        return new KakaoTokenRequest(code);
    }
}
