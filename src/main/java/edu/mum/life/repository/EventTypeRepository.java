package edu.mum.life.repository;
import edu.mum.life.domain.EventType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the EventType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventTypeRepository extends JpaRepository<EventType, Long> {

}
