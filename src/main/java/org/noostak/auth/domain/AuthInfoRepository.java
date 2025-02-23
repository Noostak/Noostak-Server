package org.noostak.auth.domain;

import org.noostak.auth.common.exception.AuthErrorCode;
import org.noostak.auth.common.exception.AuthException;
import org.noostak.auth.domain.vo.AuthId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthInfoRepository extends JpaRepository<AuthInfo, Long> {

    Optional<AuthInfo> findByAuthId(AuthId authid);
    boolean existsAuthInfoByAuthId(AuthId code);

    default boolean hasAuthInfoByAuthId(AuthId authid){
        return this.existsAuthInfoByAuthId(authid);
    }

    default AuthInfo getAuthInfoByAuthId(AuthId authid){
        return this.findByAuthId(authid)
                .orElseThrow(()->new AuthException(AuthErrorCode.AUTH_ID_NOT_EXISTS, authid));
    }
}
