package edu.mum.life.service.impl;

import edu.mum.life.service.RSVPRecordService;
import edu.mum.life.domain.RSVPRecord;
import edu.mum.life.repository.RSVPRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link RSVPRecord}.
 */
@Service
@Transactional
public class RSVPRecordServiceImpl implements RSVPRecordService {

    private final Logger log = LoggerFactory.getLogger(RSVPRecordServiceImpl.class);

    private final RSVPRecordRepository rSVPRecordRepository;

    public RSVPRecordServiceImpl(RSVPRecordRepository rSVPRecordRepository) {
        this.rSVPRecordRepository = rSVPRecordRepository;
    }

    /**
     * Save a rSVPRecord.
     *
     * @param rSVPRecord the entity to save.
     * @return the persisted entity.
     */
    @Override
    public RSVPRecord save(RSVPRecord rSVPRecord) {
        log.debug("Request to save RSVPRecord : {}", rSVPRecord);
        return rSVPRecordRepository.save(rSVPRecord);
    }

    /**
     * Get all the rSVPRecords.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RSVPRecord> findAll(Pageable pageable) {
        log.debug("Request to get all RSVPRecords");
        return rSVPRecordRepository.findAll(pageable);
    }


    /**
     * Get one rSVPRecord by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<RSVPRecord> findOne(Long id) {
        log.debug("Request to get RSVPRecord : {}", id);
        return rSVPRecordRepository.findById(id);
    }

    /**
     * Delete the rSVPRecord by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete RSVPRecord : {}", id);
        rSVPRecordRepository.deleteById(id);
    }
}
