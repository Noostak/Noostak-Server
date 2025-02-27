package org.noostak.appointmentoption.domain;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.noostak.appointmentoption.domain.vo.AppointmentOptionStatus;
import org.springframework.data.domain.*;
import org.springframework.data.repository.query.FluentQuery;
import java.util.function.Function;

public class AppointmentOptionRepositoryTest implements AppointmentOptionRepository {

    private final List<AppointmentOption> appointmentOptions = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public AppointmentOption save(AppointmentOption entity) {
        if (entity.getId() == null) {
            try {
                var idField = AppointmentOption.class.getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(entity, idGenerator.getAndIncrement());
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException("[ERROR] 'id' 필드에 접근할 수 없습니다.", e);
            }
        }
        appointmentOptions.add(entity);
        return entity;
    }

    @Override
    public Optional<AppointmentOption> findById(Long id) {
        return appointmentOptions.stream()
                .filter(option -> id.equals(option.getId()))
                .findFirst();
    }

    @Override
    public Optional<AppointmentOption> findByAppointmentConfirmedYearAndMonth(Long appointmentId, int year, int month) {
        return appointmentOptions.stream()
                .filter(option -> option.getAppointment().getId().equals(appointmentId))
                .filter(option -> option.getStatus() == AppointmentOptionStatus.CONFIRMED)
                .filter(option -> option.getDate().getYear() == year)
                .filter(option -> option.getDate().getMonthValue() == month)
                .findFirst();
    }

    @Override
    public Optional<AppointmentOption> findByAppointmentConfirmedBetweenDate(Long appointmentId, LocalDate startDate, LocalDate endDate) {
        return appointmentOptions.stream()
                .filter(option -> option.getAppointment().getId().equals(appointmentId))
                .filter(option -> option.getStatus() == AppointmentOptionStatus.CONFIRMED)
                .filter(option -> !option.getDate().toLocalDate().isBefore(startDate) &&
                        !option.getDate().toLocalDate().isAfter(endDate))
                .findFirst();
    }

    @Override
    public boolean existsById(Long id) {
        return appointmentOptions.stream().anyMatch(option -> option.getId().equals(id));
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
    public void deleteById(Long id) {
        appointmentOptions.removeIf(option -> option.getId().equals(id));
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
