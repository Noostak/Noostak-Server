package org.noostak.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.noostak.auth.common.exception.GoogleApiErrorCode;
import org.noostak.auth.common.exception.GoogleApiException;

import java.util.List;

@Getter
@AllArgsConstructor
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GoogleTokenResponse {

    private String accessToken;
    private int expiresIn;
    private String refreshToken;
    private String tokenType;
    private String idToken;


    private int code;
    private String message;
    private List<ErrorDetail> details;

    @Getter
    @Setter
    public static class ErrorDetail {
        @JsonProperty("@type")
        private String type;
        private String reason;
        private String domain;
        private String metadata;
    }

    public void validate() {
        if (details != null && !details.isEmpty()) {
            throw new GoogleApiException(GoogleApiErrorCode.GOOGLE_API_ERROR,details);
        }
    }
}
