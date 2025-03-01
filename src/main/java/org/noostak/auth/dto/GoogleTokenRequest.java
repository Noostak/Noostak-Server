package org.noostak.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


@Getter
@ToString
public class GoogleTokenRequest {
    private String code;
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String grantType;

    private GoogleTokenRequest(String clientId, String redirectUri, String code, String clientSecret) {
        this.grantType = "authorization_code";
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.code = code;
        this.clientSecret = clientSecret;
    }

    public static GoogleTokenRequest of(String clientId, String redirectUri, String code, String clientSecret){
        return new GoogleTokenRequest(clientId,redirectUri,code,clientSecret);
    }

    public String getUrlEncodedParams() {
        StringBuilder params = new StringBuilder();

        params.append("grant_type=").append(URLEncoder.encode(grantType, StandardCharsets.UTF_8));
        params.append("&clientId=").append(URLEncoder.encode(clientId, StandardCharsets.UTF_8));
        params.append("&clientSecret=").append(URLEncoder.encode(clientSecret, StandardCharsets.UTF_8));
        params.append("&redirectUri=").append(URLEncoder.encode(redirectUri, StandardCharsets.UTF_8));
        params.append("&code=").append(URLEncoder.encode(code, StandardCharsets.UTF_8));
        params.append("&prompt=").append(URLEncoder.encode("consent", StandardCharsets.UTF_8));
        params.append("&access_type=").append(URLEncoder.encode("offline", StandardCharsets.UTF_8));

        return params.toString();
    }
}
