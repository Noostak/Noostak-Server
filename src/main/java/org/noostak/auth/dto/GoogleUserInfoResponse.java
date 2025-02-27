package org.noostak.auth.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.noostak.auth.common.exception.GoogleApiErrorCode;
import org.noostak.auth.common.exception.GoogleApiException;


@Getter
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GoogleUserInfoResponse {

    private String id;
    private String email;
    private boolean verifiedEmail;
    private String name;
    private String givenName;
    private String familyName;
    private String picture;
    private String locale;

    // 에러 응답 처리를 위한 필드 (선택적)
    @JsonProperty("error")
    private Error error;


    @AllArgsConstructor
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Error {
        private int code;
        private String message;
        private String status;
    }

    public void validate() {
        if (error != null) {
            throw new GoogleApiException(GoogleApiErrorCode.GOOGLE_API_ERROR, error.getMessage());
        }
    }
}

