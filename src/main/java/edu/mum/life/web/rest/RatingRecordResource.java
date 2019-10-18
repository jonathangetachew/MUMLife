package edu.mum.life.web.rest;

import edu.mum.life.domain.RatingRecord;
import edu.mum.life.repository.RatingRecordRepository;
import edu.mum.life.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link edu.mum.life.domain.RatingRecord}.
 */
@RestController
@RequestMapping("/api")
public class RatingRecordResource {

    private final Logger log = LoggerFactory.getLogger(RatingRecordResource.class);

    private static final String ENTITY_NAME = "ratingRecord";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RatingRecordRepository ratingRecordRepository;

    public RatingRecordResource(RatingRecordRepository ratingRecordRepository) {
        this.ratingRecordRepository = ratingRecordRepository;
    }

    /**
     * {@code POST  /rating-records} : Create a new ratingRecord.
     *
     * @param ratingRecord the ratingRecord to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ratingRecord, or with status {@code 400 (Bad Request)} if the ratingRecord has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rating-records")
    public ResponseEntity<RatingRecord> createRatingRecord(@RequestBody RatingRecord ratingRecord) throws URISyntaxException {
        log.debug("REST request to save RatingRecord : {}", ratingRecord);
        if (ratingRecord.getId() != null) {
            throw new BadRequestAlertException("A new ratingRecord cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RatingRecord result = ratingRecordRepository.save(ratingRecord);
        return ResponseEntity.created(new URI("/api/rating-records/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rating-records} : Updates an existing ratingRecord.
     *
     * @param ratingRecord the ratingRecord to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ratingRecord,
     * or with status {@code 400 (Bad Request)} if the ratingRecord is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ratingRecord couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rating-records")
    public ResponseEntity<RatingRecord> updateRatingRecord(@RequestBody RatingRecord ratingRecord) throws URISyntaxException {
        log.debug("REST request to update RatingRecord : {}", ratingRecord);
        if (ratingRecord.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RatingRecord result = ratingRecordRepository.save(ratingRecord);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, ratingRecord.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /rating-records} : get all the ratingRecords.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ratingRecords in body.
     */
    @GetMapping("/rating-records")
    public List<RatingRecord> getAllRatingRecords() {
        log.debug("REST request to get all RatingRecords");
        return ratingRecordRepository.findAll();
    }

    /**
     * {@code GET  /rating-records/:id} : get the "id" ratingRecord.
     *
     * @param id the id of the ratingRecord to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ratingRecord, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rating-records/{id}")
    public ResponseEntity<RatingRecord> getRatingRecord(@PathVariable Long id) {
        log.debug("REST request to get RatingRecord : {}", id);
        Optional<RatingRecord> ratingRecord = ratingRecordRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(ratingRecord);
    }

    /**
     * {@code DELETE  /rating-records/:id} : delete the "id" ratingRecord.
     *
     * @param id the id of the ratingRecord to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rating-records/{id}")
    public ResponseEntity<Void> deleteRatingRecord(@PathVariable Long id) {
        log.debug("REST request to delete RatingRecord : {}", id);
        ratingRecordRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
