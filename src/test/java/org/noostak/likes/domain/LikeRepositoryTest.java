package org.noostak.likes.domain;

import org.springframework.data.domain.*;
import org.springframework.data.repository.query.FluentQuery;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class LikeRepositoryTest implements LikeRepository {

    private final List<Like> likes = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Like save(Like entity) {
        try {
            if (entity.getId() == null) {
                var idField = Like.class.getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(entity, idGenerator.getAndIncrement());
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("[ERROR] 'id' 필드에 접근할 수 없습니다.", e);
        }
        likes.add(entity);
        return entity;
    }

    @Override
    public <S extends Like> List<S> saveAll(Iterable<S> entities) {
        List<S> result = new ArrayList<>();
        for (S entity : entities) {
            Like savedEntity = save(entity);
            result.add((S) savedEntity);
        }
        return result;
    }

    @Override
    public Optional<Like> findById(Long id) {
        return likes.stream()
                .filter(like -> like.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Like> findAll() {
        return new ArrayList<>(likes);
    }

    @Override
    public void deleteAll() {
        likes.clear();
    }

    @Override
    public void deleteById(Long id) {
        likes.removeIf(like -> like.getId().equals(id));
    }

    @Override
    public void delete(Like entity) {
        likes.remove(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Like> entities) {

    }

    @Override
    public boolean existsById(Long id) {
        return likes.stream().anyMatch(like -> like.getId().equals(id));
    }

    @Override
    public long count() {
        return likes.size();
    }

    @Override
    public List<Like> findAllById(Iterable<Long> ids) {
        return StreamSupport.stream(ids.spliterator(), false)
                .map(this::findById)
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }

    @Override
    public List<Like> findAll(Sort sort) {
        return new ArrayList<>(likes);
    }

    @Override
    public Page<Like> findAll(Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), likes.size());
        List<Like> pageContent = likes.subList(start, end);
        return new PageImpl<>(pageContent, pageable, likes.size());
    }



    @Override
    public void flush() {

    }

    @Override
    public <S extends Like> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Like> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<Like> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Like getOne(Long aLong) {
        return null;
    }

    @Override
    public Like getById(Long aLong) {
        return null;
    }

    @Override
    public Like getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Like> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Like> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends Like> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends Like> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Like> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Like> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Like, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public Long getLikeCountByOptionId(Long optionId) {
        return null;
    }

    @Override
    public int getLikeCountByAppointmentMemberId(Long appointmentMemberId) {
        return 0;
    }

    @Override
    public void deleteLikeByAppointmentMemberIdAndOptionId(Long appointmentMemberId, Long optionId) {

    }

    @Override
    public boolean getExistsByAppointmentOptionIdAndAppointmentMemberId(Long appointmentOptionId, Long appointmentMemberId) {
        return false;
    }
}
