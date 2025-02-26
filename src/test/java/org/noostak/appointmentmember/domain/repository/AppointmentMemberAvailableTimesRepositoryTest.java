package org.noostak.appointmentmember.domain.repository;

import org.noostak.appointmentmember.domain.AppointmentMember;
import org.noostak.appointmentmember.domain.AppointmentMemberAvailableTime;
import org.noostak.appointmentmember.domain.AppointmentMemberAvailableTimesRepository;
import org.springframework.data.domain.*;
import org.springframework.data.repository.query.FluentQuery;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AppointmentMemberAvailableTimesRepositoryTest implements AppointmentMemberAvailableTimesRepository {

    private final List<AppointmentMemberAvailableTime> availableTimes = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    private final Map<Long, List<AppointmentMemberAvailableTime>> appointmentIdToTimesMap = new HashMap<>();

    @Override
    public AppointmentMemberAvailableTime save(AppointmentMemberAvailableTime entity) {
        try {
            if (entity.getId() == null) {
                var idField = AppointmentMemberAvailableTime.class.getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(entity, idGenerator.getAndIncrement());
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("[ERROR] 'id' 필드에 접근할 수 없습니다.", e);
        }

        availableTimes.add(entity);
        appointmentIdToTimesMap
                .computeIfAbsent(entity.getAppointmentMember().getAppointment().getId(), k -> new ArrayList<>())
                .add(entity);

        return entity;
    }

    @Override
    public List<AppointmentMemberAvailableTime> findByAppointmentMember(AppointmentMember appointmentMember) {
        return availableTimes.stream()
                .filter(time -> time.getAppointmentMember().equals(appointmentMember))
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentMemberAvailableTime> findByAppointmentMember_AppointmentId(Long appointmentId) {
        return appointmentIdToTimesMap.getOrDefault(appointmentId, Collections.emptyList());
    }

    @Override
    public void deleteByAppointmentMember(AppointmentMember appointmentMember) {
        availableTimes.removeIf(time -> time.getAppointmentMember().equals(appointmentMember));
        appointmentIdToTimesMap.values().forEach(list -> list.removeIf(time -> time.getAppointmentMember().equals(appointmentMember)));
    }

    @Override
    public Optional<AppointmentMemberAvailableTime> findById(Long id) {
        return availableTimes.stream()
                .filter(time -> time.getId().equals(id))
                .findFirst();
    }

    @Override
    public void deleteById(Long id) {
        availableTimes.removeIf(time -> time.getId().equals(id));
        appointmentIdToTimesMap.values().forEach(list -> list.removeIf(time -> time.getId().equals(id)));
    }

    @Override
    public void delete(AppointmentMemberAvailableTime entity) {
        availableTimes.remove(entity);
        appointmentIdToTimesMap.values().forEach(list -> list.remove(entity));
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends AppointmentMemberAvailableTime> entities) {

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
    public void deleteAll() {
        availableTimes.clear();
        appointmentIdToTimesMap.clear();
    }

    @Override
    public List<AppointmentMemberAvailableTime> findAll() {
        return new ArrayList<>(availableTimes);
    }

    @Override
    public List<AppointmentMemberAvailableTime> findAllById(Iterable<Long> ids) {
        return StreamSupport.stream(ids.spliterator(), false)
                .map(this::findById)
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }

    @Override
    public <S extends AppointmentMemberAvailableTime> List<S> saveAll(Iterable<S> entities) {
        List<S> result = new ArrayList<>();
        for (S entity : entities) {
            result.add((S) save(entity));
        }
        return result;
    }

    @Override
    public List<AppointmentMemberAvailableTime> findAll(Sort sort) {
        return new ArrayList<>(availableTimes);
    }

    @Override
    public Page<AppointmentMemberAvailableTime> findAll(Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), availableTimes.size());
        List<AppointmentMemberAvailableTime> pageContent = availableTimes.subList(start, end);
        return new PageImpl<>(pageContent, pageable, availableTimes.size());
    }

    // 사용하지 않는 메서드는 빈 구현으로 둠
    @Override
    public void flush() {}

    @Override
    public <S extends AppointmentMemberAvailableTime> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends AppointmentMemberAvailableTime> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<AppointmentMemberAvailableTime> entities) {}

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {}

    @Override
    public void deleteAllInBatch() {
        availableTimes.clear();
        appointmentIdToTimesMap.clear();
    }

    @Override
    public AppointmentMemberAvailableTime getOne(Long aLong) {
        return null;
    }

    @Override
    public AppointmentMemberAvailableTime getById(Long aLong) {
        return null;
    }

    @Override
    public AppointmentMemberAvailableTime getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends AppointmentMemberAvailableTime> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends AppointmentMemberAvailableTime> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends AppointmentMemberAvailableTime> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends AppointmentMemberAvailableTime> Page<S> findAll(Example<S> example, Pageable pageable) {
        return Page.empty();
    }

    @Override
    public <S extends AppointmentMemberAvailableTime> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends AppointmentMemberAvailableTime> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends AppointmentMemberAvailableTime, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
