package edu.mum.life.repository;
import edu.mum.life.domain.Item;
import edu.mum.life.domain.enumeration.ItemStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Item entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Page<Item> findAllByStatus(ItemStatus status, Pageable pageable);
}
