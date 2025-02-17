package org.noostak.appointment.domain;

import org.springframework.data.domain.*;
import org.springframework.data.repository.query.FluentQuery;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.StreamSupport;

public class AppointmentRepositoryTest implements AppointmentRepository {

    private final List<Appointment> appointments = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Appointment save(Appointment entity) {
        try {
            if (entity.getAppointmentId() == null) {
                var idField = Appointment.class.getDeclaredField("appointmentId");
                idField.setAccessible(true);
                idField.set(entity, idGenerator.getAndIncrement());
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("[ERROR] 'appointmentId' 필드에 접근할 수 없습니다.", e);
        }
        appointments.add(entity);
        return entity;
    }

    @Override
    public <S extends Appointment> List<S> saveAll(Iterable<S> entities) {
        List<S> result = new ArrayList<>();
        for (S entity : entities) {
            Appointment savedEntity = save(entity);
            result.add((S) savedEntity);
        }
        return result;
    }


    @Override
    public Optional<Appointment> findById(Long id) {
        return appointments.stream()
                .filter(appointment -> appointment.getAppointmentId().equals(id))
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
        appointments.removeIf(appointment -> appointment.getAppointmentId().equals(id));
    }

    @Override
    public void delete(Appointment entity) {
        appointments.remove(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> ids) {
        StreamSupport.stream(ids.spliterator(), false)
                .forEach(this::deleteById);
    }

    @Override
    public void deleteAll(Iterable<? extends Appointment> entities) {
        StreamSupport.stream(entities.spliterator(), false)
                .forEach(appointments::remove);
    }

    @Override
    public boolean existsById(Long id) {
        return appointments.stream().anyMatch(appointment -> appointment.getAppointmentId().equals(id));
    }

    @Override
    public long count() {
        return appointments.size();
    }

    @Override
    public List<Appointment> findAllById(Iterable<Long> ids) {
        List<Appointment> result = new ArrayList<>();
        StreamSupport.stream(ids.spliterator(), false)
                .map(this::findById)
                .flatMap(Optional::stream)
                .forEach(result::add);
        return result;
    }

    @Override
    public List<Appointment> findAll(Sort sort) {
        return new ArrayList<>(appointments);
    }

    @Override
    public Page<Appointment> findAll(Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), appointments.size());
        List<Appointment> pageContent = appointments.subList(start, end);
        return new PageImpl<>(pageContent, pageable, appointments.size());
    }

    @Override
    public void flush() {}

    @Override
    public <S extends Appointment> List<S> saveAllAndFlush(Iterable<S> entities) {
        return saveAll(entities);
    }

    @Override
    public Appointment saveAndFlush(Appointment entity) {
        return save(entity);
    }

    @Override
    public void deleteAllInBatch(Iterable<Appointment> entities) {
        StreamSupport.stream(entities.spliterator(), false)
                .forEach(appointments::remove);
    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> ids) {
        StreamSupport.stream(ids.spliterator(), false)
                .forEach(this::deleteById);
    }

    @Override
    public void deleteAllInBatch() {
        appointments.clear();
    }

    @Override
    public Appointment getOne(Long id) {
        return findById(id).orElse(null);
    }

    @Override
    public Appointment getById(Long id) {
        return findById(id).orElse(null);
    }

    @Override
    public Appointment getReferenceById(Long id) {
        return findById(id).orElse(null);
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
        return Page.empty();
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
