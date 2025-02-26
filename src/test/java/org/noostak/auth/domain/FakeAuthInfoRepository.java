package org.noostak.auth.domain;

import org.noostak.auth.domain.vo.AuthId;
import org.noostak.auth.domain.vo.RefreshToken;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class FakeAuthInfoRepository implements AuthInfoRepository {

    private final Map<AuthId, AuthInfo> storage = new HashMap<>();

    @Override
    public AuthInfo save(AuthInfo authInfo) {
        storage.put(authInfo.getAuthId(), authInfo);
        return authInfo;
    }

    @Override
    public AuthInfo getAuthInfoByAuthId(AuthId authId) {
        return storage.get(authId);
    }

    @Override
    public Optional<AuthInfo> findByAuthId(AuthId authid) {
        return Optional.empty();
    }

    @Override
    public boolean existsAuthInfoByAuthId(AuthId code) {
        return false;
    }

    @Override
    public boolean hasAuthInfoByAuthId(AuthId authId) {
        return storage.containsKey(authId);
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends AuthInfo> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends AuthInfo> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<AuthInfo> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public AuthInfo getOne(Long aLong) {
        return null;
    }

    @Override
    public AuthInfo getById(Long aLong) {
        return null;
    }

    @Override
    public AuthInfo getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends AuthInfo> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends AuthInfo> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends AuthInfo> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends AuthInfo> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends AuthInfo> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends AuthInfo> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends AuthInfo, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends AuthInfo> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<AuthInfo> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public List<AuthInfo> findAll() {
        return null;
    }

    @Override
    public List<AuthInfo> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(AuthInfo entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends AuthInfo> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<AuthInfo> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<AuthInfo> findAll(Pageable pageable) {
        return null;
    }
}
