package org.noostak.auth.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.noostak.auth.common.exception.GoogleApiErrorCode;
import org.noostak.auth.common.exception.GoogleApiException;
import org.noostak.auth.common.exception.KakaoApiErrorCode;
import org.noostak.auth.common.exception.KakaoApiException;

import java.util.List;

@Getter
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GoogleAccessTokenResponse {
    private String tokenType;
    private String accessToken;
    private String refreshToken;
    private String scope;
    private int expiresIn;

    private String error;
    private String error_description;

    public void validate() {
        if (!(error == null || error.isEmpty())) {
            throw new GoogleApiException(GoogleApiErrorCode.GOOGLE_API_ERROR,error+","+error_description);
        }
    }
}
