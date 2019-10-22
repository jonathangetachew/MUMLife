package edu.mum.life.repository;
import edu.mum.life.domain.ReservationRecord;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the ReservationRecord entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReservationRecordRepository extends JpaRepository<ReservationRecord, Long> {

    @Query("select reservationRecord from ReservationRecord reservationRecord where reservationRecord.user.username = ?#{principal.username}")
    List<ReservationRecord> findByUserIsCurrentUser();

}
