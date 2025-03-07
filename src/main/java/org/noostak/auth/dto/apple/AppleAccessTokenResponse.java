package org.noostak.auth.dto.apple;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.noostak.auth.common.exception.GoogleApiErrorCode;
import org.noostak.auth.common.exception.GoogleApiException;

@Getter
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AppleAccessTokenResponse {
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
