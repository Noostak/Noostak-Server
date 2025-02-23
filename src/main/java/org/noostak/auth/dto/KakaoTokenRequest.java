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
public class KakaoTokenRequest {

    private String grantType;

    private String clientId;

    private String redirectUri;

    private String code;

    private String clientSecret;

    private KakaoTokenRequest(String clientId, String redirectUri, String code, String clientSecret) {
        this.grantType = "authorization_code";
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.code = code;
        this.clientSecret = clientSecret;
    }

    public static KakaoTokenRequest of(String clientId, String redirectUri, String code, String clientSecret){
        return new KakaoTokenRequest(clientId,redirectUri,code, clientSecret);
    }

    public String getUrlEncodedParams() {
        StringBuilder params = new StringBuilder();

        params.append("grant_type=").append(URLEncoder.encode(grantType, StandardCharsets.UTF_8));
        params.append("&client_id=").append(URLEncoder.encode(clientId, StandardCharsets.UTF_8));
        params.append("&redirect_uri=").append(URLEncoder.encode(redirectUri, StandardCharsets.UTF_8));
        params.append("&code=").append(URLEncoder.encode(code, StandardCharsets.UTF_8));
        params.append("&client_secret=").append(URLEncoder.encode(clientSecret, StandardCharsets.UTF_8));

        return params.toString();
    }
}
