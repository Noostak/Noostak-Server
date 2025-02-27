package org.noostak.appointment.domain;

import org.noostak.appointment.domain.vo.AppointmentStatus;
import org.springframework.data.domain.*;
import org.springframework.data.repository.query.FluentQuery;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AppointmentRepositoryTest implements AppointmentRepository, AppointmentRepositoryCustom {

    private final List<Appointment> appointments = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Appointment save(Appointment entity) {
        if (entity.getId() == null) {
            setEntityId(entity);
        }
        appointments.add(entity);
        return entity;
    }

    @Override
    public List<Appointment> findAllByGroupId(Long groupId) {
        return appointments.stream()
                .filter(appointment -> appointment.getGroup().getId().equals(groupId))
                .sorted(Comparator.comparing(Appointment::getCreatedAt, Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Appointment> findAllByGroupIdConfirmed(AppointmentStatus status, Long groupId) {
        return List.of();
    }

    @Override
    public List<Appointment> findAllByGroupIdConfirmed(String status, Long groupId) {
        return appointments.stream()
                .filter(appointment -> appointment.getGroup().getId().equals(groupId))
                .filter(appointment -> appointment.getAppointmentStatus().name().equals(status))
                .sorted(Comparator.comparing(Appointment::getCreatedAt, Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Appointment> findById(Long id) {
        return appointments.stream()
                .filter(appointment -> appointment.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Appointment> findAll() {
        return new ArrayList<>(appointments);
    }

    @Override
    public void deleteAll() {
        appointments.clear();
    }

    @Override
    public void deleteById(Long id) {
        appointments.removeIf(appointment -> appointment.getId().equals(id));
    }

    @Override
    public void delete(Appointment entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Appointment> entities) {

    }

    @Override
    public boolean existsById(Long id) {
        return findById(id).isPresent();
    }

    @Override
    public long count() {
        return appointments.size();
    }

    /**
     * 엔터티 ID를 설정하는 메서드 (리플렉션 사용)
     */
    private void setEntityId(Appointment entity) {
        try {
            var idField = Appointment.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(entity, idGenerator.getAndIncrement());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("[ERROR] 'appointmentId' 필드에 접근할 수 없습니다.", e);
        }
    }

    @Override
    public void flush() {}

    @Override
    public <S extends Appointment> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Appointment> List<S> saveAllAndFlush(Iterable<S> entities) {
        return saveAll(entities);
    }

    @Override
    public void deleteAllInBatch(Iterable<Appointment> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Appointment getOne(Long aLong) {
        return null;
    }

    @Override
    public Appointment getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Appointment> List<S> saveAll(Iterable<S> entities) {
        List<S> savedEntities = new ArrayList<>();
        for (S entity : entities) {
            savedEntities.add((S) save(entity));
        }
        return savedEntities;
    }

    @Override
    public List<Appointment> findAllById(Iterable<Long> ids) {
        return appointments.stream()
                .filter(appointment -> StreamSupport.stream(ids.spliterator(), false).anyMatch(id -> id.equals(appointment.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public List<Appointment> findAll(Sort sort) {
        return appointments.stream()
                .sorted((a1, a2) -> {
                    Comparator<Appointment> comparator = Comparator.comparing(Appointment::getCreatedAt, Comparator.nullsLast(Comparator.naturalOrder()));
                    return sort.isSorted() ? comparator.reversed().compare(a1, a2) : 0;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Page<Appointment> findAll(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();
        List<Appointment> sortedAppointments = appointments.stream()
                .sorted(Comparator.comparing(Appointment::getCreatedAt, Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                .skip((long) pageNumber * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());
        return new PageImpl<>(sortedAppointments, pageable, appointments.size());
    }

    @Override
    public <S extends Appointment> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Appointment> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends Appointment> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends Appointment> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Appointment> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Appointment> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Appointment, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
