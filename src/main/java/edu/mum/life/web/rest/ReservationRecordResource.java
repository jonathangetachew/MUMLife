package edu.mum.life.web.rest;

import edu.mum.life.domain.ReservationRecord;
import edu.mum.life.service.ReservationRecordService;
import edu.mum.life.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link edu.mum.life.domain.ReservationRecord}.
 */
@RestController
@RequestMapping("/api")
public class ReservationRecordResource {

    private final Logger log = LoggerFactory.getLogger(ReservationRecordResource.class);

    private static final String ENTITY_NAME = "reservationRecord";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReservationRecordService reservationRecordService;

    public ReservationRecordResource(ReservationRecordService reservationRecordService) {
        this.reservationRecordService = reservationRecordService;
    }

    /**
     * {@code POST  /reservation-records} : Create a new reservationRecord.
     *
     * @param reservationRecord the reservationRecord to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reservationRecord, or with status {@code 400 (Bad Request)} if the reservationRecord has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/reservation-records")
    public ResponseEntity<ReservationRecord> createReservationRecord(@Valid @RequestBody ReservationRecord reservationRecord) throws URISyntaxException {
        log.debug("REST request to save ReservationRecord : {}", reservationRecord);
        if (reservationRecord.getId() != null) {
            throw new BadRequestAlertException("A new reservationRecord cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReservationRecord result = reservationRecordService.save(reservationRecord);
        return ResponseEntity.created(new URI("/api/reservation-records/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /reservation-records} : Updates an existing reservationRecord.
     *
     * @param reservationRecord the reservationRecord to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reservationRecord,
     * or with status {@code 400 (Bad Request)} if the reservationRecord is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reservationRecord couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/reservation-records")
    public ResponseEntity<ReservationRecord> updateReservationRecord(@Valid @RequestBody ReservationRecord reservationRecord) throws URISyntaxException {
        log.debug("REST request to update ReservationRecord : {}", reservationRecord);
        if (reservationRecord.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ReservationRecord result = reservationRecordService.save(reservationRecord);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, reservationRecord.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /reservation-records} : get all the reservationRecords.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reservationRecords in body.
     */
    @GetMapping("/reservation-records")
    public ResponseEntity<List<ReservationRecord>> getAllReservationRecords(Pageable pageable) {
        log.debug("REST request to get a page of ReservationRecords");
        Page<ReservationRecord> page = reservationRecordService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /reservation-records/:id} : get the "id" reservationRecord.
     *
     * @param id the id of the reservationRecord to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reservationRecord, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/reservation-records/{id}")
    public ResponseEntity<ReservationRecord> getReservationRecord(@PathVariable Long id) {
        log.debug("REST request to get ReservationRecord : {}", id);
        Optional<ReservationRecord> reservationRecord = reservationRecordService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reservationRecord);
    }

    /**
     * {@code DELETE  /reservation-records/:id} : delete the "id" reservationRecord.
     *
     * @param id the id of the reservationRecord to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/reservation-records/{id}")
    public ResponseEntity<Void> deleteReservationRecord(@PathVariable Long id) {
        log.debug("REST request to delete ReservationRecord : {}", id);
        reservationRecordService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
