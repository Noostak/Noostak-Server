package org.noostak.appointmentmember.domain.repository;

import org.noostak.appointmentmember.domain.AppointmentMember;
import org.springframework.data.domain.*;
import org.springframework.data.repository.query.FluentQuery;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

public class AppointmentMemberRepositoryTest implements AppointmentMemberRepository {

    private final List<AppointmentMember> appointmentMembers = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public AppointmentMember save(AppointmentMember entity) {
        try {
            if (entity.getId() == null) {
                var idField = AppointmentMember.class.getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(entity, idGenerator.getAndIncrement());
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("[ERROR] 'id' 필드에 접근할 수 없습니다.", e);
        }
        appointmentMembers.add(entity);
        return entity;
    }

    @Override
    public Optional<AppointmentMember> findByMemberIdAndAppointmentId(Long memberId, Long appointmentId) {
        return appointmentMembers.stream()
                .filter(member -> member.getMember().getId().equals(memberId) && member.getAppointment().getId().equals(appointmentId))
                .findFirst();
    }

    @Override
    public Optional<AppointmentMember> findById(Long id) {
        return appointmentMembers.stream().filter(member -> member.getId().equals(id)).findFirst();
    }

    @Override
    public List<AppointmentMember> findAll() {
        return new ArrayList<>(appointmentMembers);
    }

    @Override
    public List<AppointmentMember> findAllById(Iterable<Long> longs) {
        return List.of();
    }

    @Override
    public void deleteAll() {
        appointmentMembers.clear();
    }

    @Override
    public void deleteById(Long id) {
        appointmentMembers.removeIf(member -> member.getId().equals(id));
    }

    @Override
    public boolean existsById(Long id) {
        return appointmentMembers.stream().anyMatch(member -> member.getId().equals(id));
    }

    @Override
    public long count() {
        return appointmentMembers.size();
    }

    @Override
    public void delete(AppointmentMember entity) {
        appointmentMembers.remove(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends AppointmentMember> entities) {

    }

    @Override
    public <S extends AppointmentMember> List<S> saveAll(Iterable<S> entities) {
        List<S> result = new ArrayList<>();
        for (S entity : entities) {
            result.add((S) save(entity));
        }
        return result;
    }

    @Override
    public void flush() {}

    @Override
    public <S extends AppointmentMember> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends AppointmentMember> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<AppointmentMember> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {
        appointmentMembers.clear();
    }

    @Override
    public AppointmentMember getOne(Long aLong) {
        return null;
    }

    @Override
    public AppointmentMember getById(Long aLong) {
        return null;
    }

    @Override
    public AppointmentMember getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends AppointmentMember> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends AppointmentMember> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends AppointmentMember> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends AppointmentMember> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends AppointmentMember> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends AppointmentMember> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends AppointmentMember, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public List<AppointmentMember> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<AppointmentMember> findAll(Pageable pageable) {
        return null;
    }
}
