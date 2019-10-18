package edu.mum.life.repository;
import edu.mum.life.domain.RatingRecord;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the RatingRecord entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RatingRecordRepository extends JpaRepository<RatingRecord, Long> {

    @Query("select ratingRecord from RatingRecord ratingRecord where ratingRecord.user.username = ?#{principal.username}")
    List<RatingRecord> findByUserIsCurrentUser();

}
