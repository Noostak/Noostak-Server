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
        return entity;
    }

    @Override
    public Optional<AppointmentOption> findById(Long id) {
        return appointmentOptions.stream()
                .filter(option -> option.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<AppointmentOption> findAll() {
        return new ArrayList<>(appointmentOptions);
    }

    @Override
    public List<AppointmentOption> findAllById(Iterable<Long> ids) {
        Set<Long> idSet = new HashSet<>();
        ids.forEach(idSet::add);

        return appointmentOptions.stream()
                .filter(option -> idSet.contains(option.getId()))
                .collect(Collectors.toList());
    }


    @Override
    public void deleteAll() {
        appointmentOptions.clear();
    }

    @Override
    public void deleteById(Long id) {
        appointmentOptions.removeIf(option -> option.getId().equals(id));
    }

    @Override
    public boolean existsById(Long id) {
        return appointmentOptions.stream().anyMatch(option -> option.getId().equals(id));
    }

    @Override
    public long count() {
        return appointmentOptions.size();
    }

    @Override
    public void delete(AppointmentOption entity) {
        appointmentOptions.remove(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> ids) {}

    @Override
    public void deleteAll(Iterable<? extends AppointmentOption> entities) {}

    @Override
    public <S extends AppointmentOption> List<S> saveAll(Iterable<S> entities) {
        List<S> result = new ArrayList<>();
        for (S entity : entities) {
            result.add((S) save(entity));
        }
        return result;
    }

    @Override
    public void flush() {}

    @Override
    public <S extends AppointmentOption> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends AppointmentOption> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<AppointmentOption> entities) {}

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> ids) {}

    @Override
    public void deleteAllInBatch() {
        appointmentOptions.clear();
    }

    @Override
    public AppointmentOption getOne(Long id) {
        return null;
    }

    @Override
    public AppointmentOption getById(Long id) {
        return null;
    }

    @Override
    public AppointmentOption getReferenceById(Long id) {
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
