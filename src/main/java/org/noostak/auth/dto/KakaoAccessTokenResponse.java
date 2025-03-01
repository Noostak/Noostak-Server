package org.noostak.auth.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.noostak.auth.common.exception.KakaoApiErrorCode;
import org.noostak.auth.common.exception.KakaoApiException;

@Getter
@AllArgsConstructor
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoAccessTokenResponse {
    private String tokenType;
    private String accessToken;
    private String idToken;
    private int expiresIn;
    private String refreshToken; // 만약 갱신이 필요할 경우 포함되어 전달됨
    private String refreshTokenExpiresIn;

    private String error;
    private String errorDescription;
    private String errorCode;

    public boolean refreshTokenIsUpdated(){
        return refreshToken != null;
    }

    public void validate(){
        if(error!= null){
            throw new KakaoApiException(KakaoApiErrorCode.KAKAO_API_ERROR, errorDescription);
        }
    }
}
