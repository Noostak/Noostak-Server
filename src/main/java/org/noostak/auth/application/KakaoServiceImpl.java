package org.noostak.auth.application;


import lombok.RequiredArgsConstructor;
import org.noostak.auth.application.jwt.JwtToken;
import org.noostak.auth.application.jwt.JwtTokenProvider;
import org.noostak.auth.dto.KakaoFetchUserInfoResponse;
import org.noostak.auth.dto.KakaoTokenRequest;
import org.noostak.auth.dto.KakaoTokenResponse;
import org.noostak.auth.dto.SocialLoginResponse;
import org.noostak.member.application.MemberService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class KakaoServiceImpl implements KakaoService{

    private final MemberService memberService;

    @Override
    public void refreshAccessToken() {

    }

    @Override
    public SocialLoginResponse fetchUserInfo(String accessToken) {
        String requestUrl = KaKaoApi.FETCH_USER_INFO_REQUEST.getUrl();

        KakaoFetchUserInfoResponse response = postRequest(
                requestUrl,
                makeAuthorizationHeader(accessToken),
                KakaoFetchUserInfoResponse.class
        );

        // TODO: 만약 조회 실패에 대한 에러코드를 반환하는 경우(액세스 토큰 만료, 혹은 유효하지 않은 값)

        return null;
    }

    @Override
    public JwtToken requestToken(String authId) {
        String url = KaKaoApi.TOKEN_REQUEST.getUrl();
        KakaoTokenResponse response =
                postRequest(url, KakaoTokenRequest.of(authId), KakaoTokenResponse.class);

        response.validate();

        return JwtTokenProvider.createToken(response.getAccessToken(),response.getRefreshToken());
    }

    @Override
    public void login() {

    }

    private <T,R> T postRequest(String url, R request, Class<T> responseType){
        return new RestTemplate().postForObject(
                url,
                request,
                responseType
        );
    }

    private <T,R> T postRequest(String url, HttpHeaders headers, R request, Class<T> responseType){
        HttpEntity<R> requestHttpEntity = new HttpEntity<>(request, headers);

        return new RestTemplate().postForObject(
                url,
                requestHttpEntity,
                responseType
        );
    }

    private <T> T postRequest(String url, HttpHeaders headers, Class<T> responseType){
        return new RestTemplate().postForObject(
                url,
                headers,
                responseType
        );
    }

    private HttpHeaders makeAuthorizationHeader(String token){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.set("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        return new HttpHeaders();
    }
}
