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
public class KakaoLogoutRequest {

    private String clientId;

    private String logoutRedirectUri;


    private KakaoLogoutRequest(String clientId, String logoutRedirectUri) {
        this.clientId = clientId;
        this.logoutRedirectUri= logoutRedirectUri;
    }

    public static KakaoLogoutRequest of(String clientId, String logoutRedirectUri){
        return new KakaoLogoutRequest(clientId,logoutRedirectUri);
    }

    public String getUrlEncodedParams() {
        StringBuilder params = new StringBuilder();

        params.append("&client_id=").append(URLEncoder.encode(clientId, StandardCharsets.UTF_8));
        params.append("&logout_redirect_uri=").append(URLEncoder.encode(logoutRedirectUri, StandardCharsets.UTF_8));

        return params.toString();
    }
}
