package edu.mum.life.service;

import edu.mum.life.domain.CheckoutRecord;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link CheckoutRecord}.
 */
public interface CheckoutRecordService {

    /**
     * Save a checkoutRecord.
     *
     * @param checkoutRecord the entity to save.
     * @return the persisted entity.
     */
    CheckoutRecord save(CheckoutRecord checkoutRecord);

    /**
     * Get all the checkoutRecords.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CheckoutRecord> findAll(Pageable pageable);


    /**
     * Get the "id" checkoutRecord.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CheckoutRecord> findOne(Long id);

    /**
     * Delete the "id" checkoutRecord.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
