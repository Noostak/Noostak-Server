package org.noostak.auth.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.ToString;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Getter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GoogleAccessTokenRequest {

    private String grantType;

    private String clientId;

    private String refreshToken;

    private String clientSecret;

    private GoogleAccessTokenRequest(String clientId, String refreshToken, String clientSecret) {
        this.grantType = "refresh_token";
        this.clientId = clientId;
        this.refreshToken = refreshToken;
        this.clientSecret = clientSecret;
    }

    public static GoogleAccessTokenRequest of(String clientId, String refreshToken, String clientSecret){
        return new GoogleAccessTokenRequest(clientId,refreshToken,clientSecret);
    }

    public String getUrlEncodedParams() {
        StringBuilder params = new StringBuilder();

        params.append("grant_type=").append(URLEncoder.encode(grantType, StandardCharsets.UTF_8));
        params.append("&client_id=").append(URLEncoder.encode(clientId, StandardCharsets.UTF_8));
        params.append("&refresh_token=").append(URLEncoder.encode(refreshToken, StandardCharsets.UTF_8));
        params.append("&client_secret=").append(URLEncoder.encode(clientSecret, StandardCharsets.UTF_8));

        return params.toString();
    }
}
