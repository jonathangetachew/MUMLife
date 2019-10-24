package edu.mum.life.service.impl;

import edu.mum.life.service.CheckoutRecordService;
import edu.mum.life.domain.CheckoutRecord;
import edu.mum.life.repository.CheckoutRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link CheckoutRecord}.
 */
@Service
@Transactional
public class CheckoutRecordServiceImpl implements CheckoutRecordService {

    private final Logger log = LoggerFactory.getLogger(CheckoutRecordServiceImpl.class);

    private final CheckoutRecordRepository checkoutRecordRepository;

    public CheckoutRecordServiceImpl(CheckoutRecordRepository checkoutRecordRepository) {
        this.checkoutRecordRepository = checkoutRecordRepository;
    }

    /**
     * Save a checkoutRecord.
     *
     * @param checkoutRecord the entity to save.
     * @return the persisted entity.
     */
    @Override
    public CheckoutRecord save(CheckoutRecord checkoutRecord) {
        log.debug("Request to save CheckoutRecord : {}", checkoutRecord);
        return checkoutRecordRepository.save(checkoutRecord);
    }

    /**
     * Get all the checkoutRecords.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CheckoutRecord> findAll(Pageable pageable) {
        log.debug("Request to get all CheckoutRecords");
        return checkoutRecordRepository.findAll(pageable);
    }


    /**
     * Get one checkoutRecord by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CheckoutRecord> findOne(Long id) {
        log.debug("Request to get CheckoutRecord : {}", id);
        return checkoutRecordRepository.findById(id);
    }

    /**
     * Delete the checkoutRecord by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CheckoutRecord : {}", id);
        checkoutRecordRepository.deleteById(id);
    }
}
