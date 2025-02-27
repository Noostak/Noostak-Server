package org.noostak.appointment.domain;

import org.springframework.data.domain.*;
import org.springframework.data.repository.query.FluentQuery;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

public class AppointmentHostSelectionTimeRepositoryTest implements AppointmentHostSelectionTimeRepository {

    private final List<AppointmentHostSelectionTime> selectionTimes = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public AppointmentHostSelectionTime save(AppointmentHostSelectionTime entity) {
        try {
            if (entity.getId() == null) {
                var idField = AppointmentHostSelectionTime.class.getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(entity, idGenerator.getAndIncrement());
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("[ERROR] 'id' 필드에 접근할 수 없습니다.", e);
        }
        selectionTimes.add(entity);
        return entity;
    }

    @Override
    public List<AppointmentHostSelectionTime> findByAppointmentId(Long appointmentId) {
        return selectionTimes.stream()
                .filter(time -> time.getAppointment().getId().equals(appointmentId))
                .toList();
    }

    public void deleteByAppointmentId(Long appointmentId) {
        selectionTimes.removeIf(time -> time.getAppointment().getId().equals(appointmentId));
    }

    @Override
    public List<AppointmentHostSelectionTime> findAll() {
        return new ArrayList<>(selectionTimes);
    }

    @Override
    public List<AppointmentHostSelectionTime> findAllById(Iterable<Long> longs) {
        return List.of();
    }

    @Override
    public Optional<AppointmentHostSelectionTime> findById(Long id) {
        return selectionTimes.stream().filter(time -> time.getId().equals(id)).findFirst();
    }

    @Override
    public void deleteAll() {
        selectionTimes.clear();
    }

    @Override
    public void deleteById(Long id) {
        selectionTimes.removeIf(time -> time.getId().equals(id));
    }

    @Override
    public void delete(AppointmentHostSelectionTime entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends AppointmentHostSelectionTime> entities) {

    }

    @Override
    public boolean existsById(Long id) {
        return selectionTimes.stream().anyMatch(time -> time.getId().equals(id));
    }

    @Override
    public long count() {
        return selectionTimes.size();
    }

    @Override
    public <S extends AppointmentHostSelectionTime> List<S> saveAll(Iterable<S> entities) {
        List<S> result = new ArrayList<>();
        for (S entity : entities) {
            result.add((S) save(entity));
        }
        return result;
    }

    @Override
    public List<AppointmentHostSelectionTime> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<AppointmentHostSelectionTime> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public <S extends AppointmentHostSelectionTime> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends AppointmentHostSelectionTime> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends AppointmentHostSelectionTime> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<AppointmentHostSelectionTime> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public AppointmentHostSelectionTime getOne(Long aLong) {
        return null;
    }

    @Override
    public AppointmentHostSelectionTime getById(Long aLong) {
        return null;
    }

    @Override
    public AppointmentHostSelectionTime getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends AppointmentHostSelectionTime> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends AppointmentHostSelectionTime> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends AppointmentHostSelectionTime> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends AppointmentHostSelectionTime> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends AppointmentHostSelectionTime> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends AppointmentHostSelectionTime, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}