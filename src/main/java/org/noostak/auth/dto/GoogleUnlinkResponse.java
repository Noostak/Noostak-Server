package org.noostak.auth.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.noostak.auth.common.exception.KakaoApiErrorCode;
import org.noostak.auth.common.exception.KakaoApiException;


@Getter
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GoogleUnlinkResponse {
    private String error;
    private String errorDescription;
    private String errorCode;

    public void validate(){
        if(error!= null){
            throw new KakaoApiException(KakaoApiErrorCode.KAKAO_API_ERROR, errorDescription);
        }
    }
}

