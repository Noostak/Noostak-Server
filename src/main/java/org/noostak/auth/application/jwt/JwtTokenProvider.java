package org.noostak.auth.application.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;
    private Key secretKeyObj;

    @PostConstruct
    protected void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        secretKeyObj = Keys.hmacShaKeyFor(keyBytes);
    }

    public static JwtToken createToken(String accessToken, String refreshToken){
        return new JwtToken("Bearer",accessToken,refreshToken);
    }

    public boolean validateToken(String token) {
        try {

            Jwts.parserBuilder()
                    .setSigningKey(secretKeyObj)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public String getUserId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKeyObj)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}