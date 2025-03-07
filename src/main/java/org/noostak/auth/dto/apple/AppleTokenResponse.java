package org.noostak.auth.dto.apple;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.noostak.auth.common.exception.AppleApiErrorCode;
import org.noostak.auth.common.exception.AppleApiException;
import org.noostak.auth.common.exception.GoogleApiErrorCode;
import org.noostak.auth.common.exception.GoogleApiException;

import java.util.List;

@Getter
@AllArgsConstructor
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AppleTokenResponse {

    private String accessToken;
    private int expiresIn;
    private String refreshToken;
    private String tokenType;
    private String idToken;


    private String error;
    private String error_description;


    public void validate() {
        if (error != null && !error.isEmpty()) {
            throw new AppleApiException(AppleApiErrorCode.APPLE_API_ERROR,error);
        }
    }
}
