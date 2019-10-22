package edu.mum.life.web.rest;

import edu.mum.life.domain.CheckoutRecord;
import edu.mum.life.service.CheckoutRecordService;
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
 * REST controller for managing {@link edu.mum.life.domain.CheckoutRecord}.
 */
@RestController
@RequestMapping("/api")
public class CheckoutRecordResource {

    private final Logger log = LoggerFactory.getLogger(CheckoutRecordResource.class);

    private static final String ENTITY_NAME = "checkoutRecord";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CheckoutRecordService checkoutRecordService;

    public CheckoutRecordResource(CheckoutRecordService checkoutRecordService) {
        this.checkoutRecordService = checkoutRecordService;
    }

    /**
     * {@code POST  /checkout-records} : Create a new checkoutRecord.
     *
     * @param checkoutRecord the checkoutRecord to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new checkoutRecord, or with status {@code 400 (Bad Request)} if the checkoutRecord has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/checkout-records")
    public ResponseEntity<CheckoutRecord> createCheckoutRecord(@Valid @RequestBody CheckoutRecord checkoutRecord) throws URISyntaxException {
        log.debug("REST request to save CheckoutRecord : {}", checkoutRecord);
        if (checkoutRecord.getId() != null) {
            throw new BadRequestAlertException("A new checkoutRecord cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CheckoutRecord result = checkoutRecordService.save(checkoutRecord);
        return ResponseEntity.created(new URI("/api/checkout-records/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /checkout-records} : Updates an existing checkoutRecord.
     *
     * @param checkoutRecord the checkoutRecord to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated checkoutRecord,
     * or with status {@code 400 (Bad Request)} if the checkoutRecord is not valid,
     * or with status {@code 500 (Internal Server Error)} if the checkoutRecord couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/checkout-records")
    public ResponseEntity<CheckoutRecord> updateCheckoutRecord(@Valid @RequestBody CheckoutRecord checkoutRecord) throws URISyntaxException {
        log.debug("REST request to update CheckoutRecord : {}", checkoutRecord);
        if (checkoutRecord.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CheckoutRecord result = checkoutRecordService.save(checkoutRecord);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, checkoutRecord.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /checkout-records} : get all the checkoutRecords.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of checkoutRecords in body.
     */
    @GetMapping("/checkout-records")
    public ResponseEntity<List<CheckoutRecord>> getAllCheckoutRecords(Pageable pageable) {
        log.debug("REST request to get a page of CheckoutRecords");
        Page<CheckoutRecord> page = checkoutRecordService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /checkout-records/:id} : get the "id" checkoutRecord.
     *
     * @param id the id of the checkoutRecord to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the checkoutRecord, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/checkout-records/{id}")
    public ResponseEntity<CheckoutRecord> getCheckoutRecord(@PathVariable Long id) {
        log.debug("REST request to get CheckoutRecord : {}", id);
        Optional<CheckoutRecord> checkoutRecord = checkoutRecordService.findOne(id);
        return ResponseUtil.wrapOrNotFound(checkoutRecord);
    }

    /**
     * {@code DELETE  /checkout-records/:id} : delete the "id" checkoutRecord.
     *
     * @param id the id of the checkoutRecord to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/checkout-records/{id}")
    public ResponseEntity<Void> deleteCheckoutRecord(@PathVariable Long id) {
        log.debug("REST request to delete CheckoutRecord : {}", id);
        checkoutRecordService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
