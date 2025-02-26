package org.noostak.likes.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM likes WHERE appointment_option_id = :optionId")
    Long getLikeCountByOptionId(@Param("optionId") Long optionId);

    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM likes WHERE appointment_member_id = :appointmentMemberId")
    int getLikeCountByAppointmentMemberId(@Param("appointmentMemberId") Long appointmentMemberId);


    @Query(nativeQuery = true,
            value = "SELECT 1 " +
                    "FROM likes " +
                    "WHERE appointment_option_id = :optionId " +
                    "AND appointment_member_id = :memberId")
    boolean getExistsByAppointmentOptionIdAndAppointmentMemberId(
            @Param("optionId") Long appointmentOptionId,
            @Param("memberId") Long appointmentMemberId
    );
}