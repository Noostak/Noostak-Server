package org.noostak.appointmentmember.domain.repository;

import org.noostak.appointmentmember.domain.AppointmentMember;
import org.noostak.appointmentmember.domain.AppointmentMemberAvailableTimes;
import org.springframework.data.domain.*;
import org.springframework.data.repository.query.FluentQuery;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

public class AppointmentMemberAvailableTimesRepositoryTest implements AppointmentMemberAvailableTimesRepository {

    private final List<AppointmentMemberAvailableTimes> availableTimes = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public AppointmentMemberAvailableTimes save(AppointmentMemberAvailableTimes entity) {
        try {
            if (entity.getId() == null) {
                var idField = AppointmentMemberAvailableTimes.class.getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(entity, idGenerator.getAndIncrement());
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("[ERROR] 'id' 필드에 접근할 수 없습니다.", e);
        }
        availableTimes.add(entity);
        return entity;
    }

    @Override
    public List<AppointmentMemberAvailableTimes> findByAppointmentMember(AppointmentMember appointmentMember) {
        return availableTimes.stream()
                .filter(time -> time.getAppointmentMember().equals(appointmentMember))
                .toList();
    }

    @Override
    public void deleteByAppointmentMember(AppointmentMember appointmentMember) {
        availableTimes.removeIf(time -> time.getAppointmentMember().equals(appointmentMember));
    }

    @Override
    public List<AppointmentMemberAvailableTimes> findAll() {
        return new ArrayList<>(availableTimes);
    }

    @Override
    public List<AppointmentMemberAvailableTimes> findAllById(Iterable<Long> longs) {
        return List.of();
    }

    @Override
    public Optional<AppointmentMemberAvailableTimes> findById(Long id) {
        return availableTimes.stream().filter(time -> time.getId().equals(id)).findFirst();
    }

    @Override
    public void deleteAll() {
        availableTimes.clear();
    }

    @Override
    public void deleteById(Long id) {
        availableTimes.removeIf(time -> time.getId().equals(id));
    }

    @Override
    public boolean existsById(Long id) {
        return availableTimes.stream().anyMatch(time -> time.getId().equals(id));
    }

    @Override
    public long count() {
        return availableTimes.size();
    }

    @Override
    public void delete(AppointmentMemberAvailableTimes entity) {
        availableTimes.remove(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends AppointmentMemberAvailableTimes> entities) {

    }

    @Override
    public <S extends AppointmentMemberAvailableTimes> List<S> saveAll(Iterable<S> entities) {
        List<S> result = new ArrayList<>();
        for (S entity : entities) {
            result.add((S) save(entity));
        }
        return result;
    }

    @Override
    public void flush() {}

    @Override
    public <S extends AppointmentMemberAvailableTimes> S saveAndFlush(S entity) {
        return null;
    }


    @Override
    public <S extends AppointmentMemberAvailableTimes> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<AppointmentMemberAvailableTimes> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {
        availableTimes.clear();
    }

    @Override
    public AppointmentMemberAvailableTimes getOne(Long aLong) {
        return null;
    }

    @Override
    public AppointmentMemberAvailableTimes getById(Long aLong) {
        return null;
    }

    @Override
    public AppointmentMemberAvailableTimes getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends AppointmentMemberAvailableTimes> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends AppointmentMemberAvailableTimes> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends AppointmentMemberAvailableTimes> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends AppointmentMemberAvailableTimes> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends AppointmentMemberAvailableTimes> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends AppointmentMemberAvailableTimes> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends AppointmentMemberAvailableTimes, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public List<AppointmentMemberAvailableTimes> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<AppointmentMemberAvailableTimes> findAll(Pageable pageable) {
        return null;
    }
}
