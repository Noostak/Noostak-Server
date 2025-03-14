package org.noostak.auth.dto.kakao;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.noostak.auth.common.exception.KakaoApiErrorCode;
import org.noostak.auth.common.exception.KakaoApiException;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoUserInfoResponse {
    private String id;

    private String connectedAt;

    private Properties properties;

    private KakaoAccount kakaoAccount;

    private String error;
    private String errorDescription;
    private String errorCode;

    public void validate(){
        if(error!= null){
            throw new KakaoApiException(KakaoApiErrorCode.KAKAO_API_ERROR, errorDescription);
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Properties {
        private String nickname;
    }


    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class KakaoAccount {
        private boolean profileNicknameNeedsAgreement;

        private Profile profile;

        @Getter
        @AllArgsConstructor
        @NoArgsConstructor
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class Profile {
            private String nickname;
            private boolean isDefaultNickname;
        }
    }
}

