package org.noostak.auth.application.jwt;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class JwtToken {
    private String tokenType = "Bearer";
    private String accessToken;
    private String refreshToken;
}
