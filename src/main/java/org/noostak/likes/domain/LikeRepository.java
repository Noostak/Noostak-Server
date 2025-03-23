package org.noostak.likes.domain;

import org.noostak.appointmentmember.domain.AppointmentMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long>, LikeRepositoryCustom {
    void deleteAllByAppointmentMember(AppointmentMember appointmentMember);
}
