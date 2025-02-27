package org.noostak.appointmentoption.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentOptionRepository extends JpaRepository<AppointmentOption, Long> {
}
