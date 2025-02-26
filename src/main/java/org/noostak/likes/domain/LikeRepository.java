package org.noostak.likes.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Long countByAppointmentOptionId(Long appointmentOptionId);

    boolean existsByAppointmentOptionIdAndAppointmentMemberId(Long appointmentOptionId, Long appointmentMemberId);
}
