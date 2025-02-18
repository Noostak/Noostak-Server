package org.noostak.auth.domain;

import org.noostak.auth.common.exception.AuthErrorCode;
import org.noostak.auth.common.exception.AuthException;
import org.noostak.auth.domain.vo.Code;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthInfoRepository extends JpaRepository<AuthInfo, Long> {

    Optional<AuthInfo> findByAuthId(Code code);
    boolean existsAuthInfoByAuthId(Code code);

    default boolean hasAuthInfoByAuthId(Code code){
        return this.existsAuthInfoByAuthId(code);
    }

    default AuthInfo getAuthInfoByAuthId(Code code){
        return this.findByAuthId(code)
                .orElseThrow(()->new AuthException(AuthErrorCode.AUTH_INFO_NOT_EXISTS, code));
    }
}
