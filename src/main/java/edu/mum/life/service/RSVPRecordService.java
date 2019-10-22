package edu.mum.life.service;

import edu.mum.life.domain.RSVPRecord;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link RSVPRecord}.
 */
public interface RSVPRecordService {

    /**
     * Save a rSVPRecord.
     *
     * @param rSVPRecord the entity to save.
     * @return the persisted entity.
     */
    RSVPRecord save(RSVPRecord rSVPRecord);

    /**
     * Get all the rSVPRecords.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RSVPRecord> findAll(Pageable pageable);


    /**
     * Get the "id" rSVPRecord.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RSVPRecord> findOne(Long id);

    /**
     * Delete the "id" rSVPRecord.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
