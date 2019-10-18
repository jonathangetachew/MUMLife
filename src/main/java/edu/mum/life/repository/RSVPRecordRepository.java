package edu.mum.life.repository;
import edu.mum.life.domain.RSVPRecord;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the RSVPRecord entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RSVPRecordRepository extends JpaRepository<RSVPRecord, Long> {

    @Query("select rSVPRecord from RSVPRecord rSVPRecord where rSVPRecord.user.username = ?#{principal.username}")
    List<RSVPRecord> findByUserIsCurrentUser();

}
