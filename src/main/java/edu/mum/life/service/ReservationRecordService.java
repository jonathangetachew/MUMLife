package edu.mum.life.service;

import edu.mum.life.domain.ReservationRecord;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link ReservationRecord}.
 */
public interface ReservationRecordService {

    /**
     * Save a reservationRecord.
     *
     * @param reservationRecord the entity to save.
     * @return the persisted entity.
     */
    ReservationRecord save(ReservationRecord reservationRecord);

    /**
     * Get all the reservationRecords.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ReservationRecord> findAll(Pageable pageable);


    /**
     * Get the "id" reservationRecord.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ReservationRecord> findOne(Long id);

    /**
     * Delete the "id" reservationRecord.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
