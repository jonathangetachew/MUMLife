package edu.mum.life.repository;
import edu.mum.life.domain.CheckoutRecord;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the CheckoutRecord entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CheckoutRecordRepository extends JpaRepository<CheckoutRecord, Long> {

    @Query("select checkoutRecord from CheckoutRecord checkoutRecord where checkoutRecord.user.username = ?#{principal.username}")
    List<CheckoutRecord> findByUserIsCurrentUser();

}
