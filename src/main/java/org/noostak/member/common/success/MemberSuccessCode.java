package org.noostak.member.common.success;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.noostak.global.success.SuccessCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberSuccessCode implements SuccessCode {
    MEMBER_FETCH_COMPLETE(HttpStatus.OK,"멤버 프로필 조회에 성공했습니다.");
    ;


    private final HttpStatus status;
    private final String rawMessage;

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return  rawMessage;
    }
}
