package org.noostak.likes.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM likes WHERE appointment_option_id = :optionId")
    int getLikeCountByOptionId(@Param("optionId") Long optionId);

    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM likes WHERE appointment_member_id = :appointmentMemberId")
    int getLikeCountByAppointmentMemberId(@Param("appointmentMemberId") Long appointmentMemberId);


    @Query(nativeQuery = true,
            value = "DELETE  " +
                    "FROM likes " +
                    "WHERE appointment_member_id = :appointmentMemberId AND appointment_option_id = :optionId")
    void deleteLikeByAppointmentMemberIdAndOptionId(
            @Param("appointmentMemberId") Long appointmentMemberId,
            @Param("optionId") Long optionId
            );
}

