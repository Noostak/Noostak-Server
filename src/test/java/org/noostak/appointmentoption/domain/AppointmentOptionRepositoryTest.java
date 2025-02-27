package org.noostak.appointmentoption.domain;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.*;
import org.springframework.data.repository.query.FluentQuery;

public class AppointmentOptionRepositoryTest implements AppointmentOptionRepository {

    private final List<AppointmentOption> appointmentOptions = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public AppointmentOption save(AppointmentOption entity) {
        try {
            if (entity.getId() == null) {
                var idField = AppointmentOption.class.getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(entity, idGenerator.getAndIncrement());
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("[ERROR] 'id' 필드에 접근할 수 없습니다.", e);
        }
        appointmentOptions.add(entity);

        // ✅ 저장 후 즉시 조회하여 데이터 유효성 검사
        Optional<AppointmentOption> savedOption = findById(entity.getId());
        if (savedOption.isEmpty()) {
            throw new RuntimeException("[ERROR] save() 후 findById()에서 데이터를 찾을 수 없습니다.");
        }
        return entity;
    }

    @Override
    public Optional<AppointmentOption> findById(Long id) {
        return appointmentOptions.stream()
                .filter(option -> id.equals(option.getId())) // ✅ equals() 비교 방식 수정
                .findFirst();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public <S extends AppointmentOption> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public List<AppointmentOption> findAll() {
        return new ArrayList<>(appointmentOptions);
    }

    @Override
    public List<AppointmentOption> findAllById(Iterable<Long> longs) {
        return List.of();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(AppointmentOption entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends AppointmentOption> entities) {

    }

    @Override
    public void deleteAll() {
        appointmentOptions.clear();
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends AppointmentOption> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends AppointmentOption> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<AppointmentOption> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public AppointmentOption getOne(Long aLong) {
        return null;
    }

    @Override
    public AppointmentOption getById(Long aLong) {
        return null;
    }

    @Override
    public AppointmentOption getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends AppointmentOption> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends AppointmentOption> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends AppointmentOption> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends AppointmentOption> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends AppointmentOption> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends AppointmentOption> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends AppointmentOption, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public List<AppointmentOption> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<AppointmentOption> findAll(Pageable pageable) {
        return null;
    }
}
