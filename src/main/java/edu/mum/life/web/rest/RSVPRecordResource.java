package edu.mum.life.web.rest;

import edu.mum.life.domain.RSVPRecord;
import edu.mum.life.repository.RSVPRecordRepository;
import edu.mum.life.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link edu.mum.life.domain.RSVPRecord}.
 */
@RestController
@RequestMapping("/api")
public class RSVPRecordResource {

    private final Logger log = LoggerFactory.getLogger(RSVPRecordResource.class);

    private static final String ENTITY_NAME = "rSVPRecord";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RSVPRecordRepository rSVPRecordRepository;

    public RSVPRecordResource(RSVPRecordRepository rSVPRecordRepository) {
        this.rSVPRecordRepository = rSVPRecordRepository;
    }

    /**
     * {@code POST  /rsvp-records} : Create a new rSVPRecord.
     *
     * @param rSVPRecord the rSVPRecord to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rSVPRecord, or with status {@code 400 (Bad Request)} if the rSVPRecord has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rsvp-records")
    public ResponseEntity<RSVPRecord> createRSVPRecord(@Valid @RequestBody RSVPRecord rSVPRecord) throws URISyntaxException {
        log.debug("REST request to save RSVPRecord : {}", rSVPRecord);
        if (rSVPRecord.getId() != null) {
            throw new BadRequestAlertException("A new rSVPRecord cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RSVPRecord result = rSVPRecordRepository.save(rSVPRecord);
        return ResponseEntity.created(new URI("/api/rsvp-records/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rsvp-records} : Updates an existing rSVPRecord.
     *
     * @param rSVPRecord the rSVPRecord to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rSVPRecord,
     * or with status {@code 400 (Bad Request)} if the rSVPRecord is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rSVPRecord couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rsvp-records")
    public ResponseEntity<RSVPRecord> updateRSVPRecord(@Valid @RequestBody RSVPRecord rSVPRecord) throws URISyntaxException {
        log.debug("REST request to update RSVPRecord : {}", rSVPRecord);
        if (rSVPRecord.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RSVPRecord result = rSVPRecordRepository.save(rSVPRecord);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, rSVPRecord.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /rsvp-records} : get all the rSVPRecords.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rSVPRecords in body.
     */
    @GetMapping("/rsvp-records")
    public List<RSVPRecord> getAllRSVPRecords() {
        log.debug("REST request to get all RSVPRecords");
        return rSVPRecordRepository.findAll();
    }

    /**
     * {@code GET  /rsvp-records/:id} : get the "id" rSVPRecord.
     *
     * @param id the id of the rSVPRecord to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rSVPRecord, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rsvp-records/{id}")
    public ResponseEntity<RSVPRecord> getRSVPRecord(@PathVariable Long id) {
        log.debug("REST request to get RSVPRecord : {}", id);
        Optional<RSVPRecord> rSVPRecord = rSVPRecordRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(rSVPRecord);
    }

    /**
     * {@code DELETE  /rsvp-records/:id} : delete the "id" rSVPRecord.
     *
     * @param id the id of the rSVPRecord to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rsvp-records/{id}")
    public ResponseEntity<Void> deleteRSVPRecord(@PathVariable Long id) {
        log.debug("REST request to delete RSVPRecord : {}", id);
        rSVPRecordRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
