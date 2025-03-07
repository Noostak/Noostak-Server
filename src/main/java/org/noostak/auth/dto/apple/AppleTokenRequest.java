package org.noostak.auth.dto.apple;

import lombok.Getter;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


@Getter
public class AppleTokenRequest {
    private String code;
    private String clientId;
    private String clientSecret;
    private String grantType;

    private AppleTokenRequest(String clientId,String code, String clientSecret) {
        this.grantType = "authorization_code";
        this.clientId = clientId;
        this.code = code;
        this.clientSecret = clientSecret;
    }

    public static AppleTokenRequest of(String clientId, String code, String clientSecret){
        return new AppleTokenRequest(clientId,code,clientSecret);
    }

    public String getUrlEncodedParams() {
        StringBuilder params = new StringBuilder();

        params.append("grant_type=").append(URLEncoder.encode(grantType, StandardCharsets.UTF_8));
        params.append("&client_id=").append(URLEncoder.encode(clientId, StandardCharsets.UTF_8));
        params.append("&client_secret=").append(URLEncoder.encode(clientSecret, StandardCharsets.UTF_8));
        params.append("&code=").append(URLEncoder.encode(code, StandardCharsets.UTF_8));

        return params.toString();
    }
}
