package org.noostak.auth.dto;


import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.noostak.auth.common.exception.KakaoApiErrorCode;
import org.noostak.auth.common.exception.KakaoApiException;

@Getter
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoTokenResponse {
    private String tokenType;
    private String accessToken;
    private String idToken;
    private int expiresIn;
    private String refreshToken;
    private String refreshTokenExpiresIn;
    private String scope;

    private String error;
    private String errorDescription;
    private String errorCode;

    public void validate(){
        if(error!= null){
            throw new KakaoApiException(KakaoApiErrorCode.KAKAO_API_ERROR, errorDescription);
        }
    }
}
