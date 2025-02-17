package org.noostak.auth.domain;

import org.noostak.auth.common.exception.AuthErrorCode;
import org.noostak.auth.common.exception.AuthException;
import org.noostak.auth.domain.vo.AuthId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthInfoRepository extends JpaRepository<AuthInfo, Long> {

    Optional<AuthInfo> findByAuthId(AuthId authId);
    boolean existsAuthInfoByAuthId(AuthId authId);

    default boolean hasAuthInfoByAuthId(AuthId authId){
        return this.existsAuthInfoByAuthId(authId);
    }

    default AuthInfo getAuthInfoByAuthId(AuthId authId){
        return this.findByAuthId(authId)
                .orElseThrow(()->new AuthException(AuthErrorCode.AUTH_INFO_NOT_EXISTS, authId));
    }
}
