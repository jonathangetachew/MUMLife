package edu.mum.life.service.impl;

import edu.mum.life.service.ReservationRecordService;
import edu.mum.life.domain.ReservationRecord;
import edu.mum.life.repository.ReservationRecordRepository;
import edu.mum.life.repository.search.ReservationRecordSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link ReservationRecord}.
 */
@Service
@Transactional
public class ReservationRecordServiceImpl implements ReservationRecordService {

    private final Logger log = LoggerFactory.getLogger(ReservationRecordServiceImpl.class);

    private final ReservationRecordRepository reservationRecordRepository;

    private final ReservationRecordSearchRepository reservationRecordSearchRepository;

    public ReservationRecordServiceImpl(ReservationRecordRepository reservationRecordRepository, ReservationRecordSearchRepository reservationRecordSearchRepository) {
        this.reservationRecordRepository = reservationRecordRepository;
        this.reservationRecordSearchRepository = reservationRecordSearchRepository;
    }

    /**
     * Save a reservationRecord.
     *
     * @param reservationRecord the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ReservationRecord save(ReservationRecord reservationRecord) {
        log.debug("Request to save ReservationRecord : {}", reservationRecord);
        ReservationRecord result = reservationRecordRepository.save(reservationRecord);
        reservationRecordSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the reservationRecords.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ReservationRecord> findAll(Pageable pageable) {
        log.debug("Request to get all ReservationRecords");
        return reservationRecordRepository.findAll(pageable);
    }


    /**
     * Get one reservationRecord by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ReservationRecord> findOne(Long id) {
        log.debug("Request to get ReservationRecord : {}", id);
        return reservationRecordRepository.findById(id);
    }

    /**
     * Delete the reservationRecord by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ReservationRecord : {}", id);
        reservationRecordRepository.deleteById(id);
        reservationRecordSearchRepository.deleteById(id);
    }

    /**
     * Search for the reservationRecord corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ReservationRecord> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ReservationRecords for query {}", query);
        return reservationRecordSearchRepository.search(queryStringQuery(query), pageable);    }
}
