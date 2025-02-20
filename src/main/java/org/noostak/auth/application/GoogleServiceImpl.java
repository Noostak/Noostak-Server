package org.noostak.auth.application;


import lombok.RequiredArgsConstructor;
import org.noostak.auth.application.jwt.JwtToken;
import org.noostak.auth.domain.vo.AuthId;
import org.noostak.auth.dto.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoogleServiceImpl implements GoogleService{

    @Override
    public void requestAccessToken(String refreshToken) {

    }

    @Override
    public TokenInfo fetchTokenInfo(String accessToken) {
        return null;
    }

    @Override
    public JwtToken requestToken(String code) {
        return null;
    }

    @Override
    public AuthId login(String accessToken) {

        return null;
    }
}
