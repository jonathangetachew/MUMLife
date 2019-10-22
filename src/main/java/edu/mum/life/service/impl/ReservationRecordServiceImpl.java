package edu.mum.life.service.impl;

import edu.mum.life.service.ReservationRecordService;
import edu.mum.life.domain.ReservationRecord;
import edu.mum.life.repository.ReservationRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ReservationRecord}.
 */
@Service
@Transactional
public class ReservationRecordServiceImpl implements ReservationRecordService {

    private final Logger log = LoggerFactory.getLogger(ReservationRecordServiceImpl.class);

    private final ReservationRecordRepository reservationRecordRepository;

    public ReservationRecordServiceImpl(ReservationRecordRepository reservationRecordRepository) {
        this.reservationRecordRepository = reservationRecordRepository;
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
        return reservationRecordRepository.save(reservationRecord);
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
    }
}
