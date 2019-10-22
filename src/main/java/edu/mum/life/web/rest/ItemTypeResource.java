package edu.mum.life.web.rest;

import edu.mum.life.domain.ItemType;
import edu.mum.life.repository.ItemTypeRepository;
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
 * REST controller for managing {@link edu.mum.life.domain.ItemType}.
 */
@RestController
@RequestMapping("/api")
public class ItemTypeResource {

    private final Logger log = LoggerFactory.getLogger(ItemTypeResource.class);

    private static final String ENTITY_NAME = "itemType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ItemTypeRepository itemTypeRepository;

    public ItemTypeResource(ItemTypeRepository itemTypeRepository) {
        this.itemTypeRepository = itemTypeRepository;
    }

    /**
     * {@code POST  /item-types} : Create a new itemType.
     *
     * @param itemType the itemType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new itemType, or with status {@code 400 (Bad Request)} if the itemType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/item-types")
    public ResponseEntity<ItemType> createItemType(@Valid @RequestBody ItemType itemType) throws URISyntaxException {
        log.debug("REST request to save ItemType : {}", itemType);
        if (itemType.getId() != null) {
            throw new BadRequestAlertException("A new itemType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ItemType result = itemTypeRepository.save(itemType);
        return ResponseEntity.created(new URI("/api/item-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /item-types} : Updates an existing itemType.
     *
     * @param itemType the itemType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemType,
     * or with status {@code 400 (Bad Request)} if the itemType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the itemType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/item-types")
    public ResponseEntity<ItemType> updateItemType(@Valid @RequestBody ItemType itemType) throws URISyntaxException {
        log.debug("REST request to update ItemType : {}", itemType);
        if (itemType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ItemType result = itemTypeRepository.save(itemType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, itemType.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /item-types} : get all the itemTypes.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of itemTypes in body.
     */
    @GetMapping("/item-types")
    public ResponseEntity<List<ItemType>> getAllItemTypes(Pageable pageable) {
        log.debug("REST request to get a page of ItemTypes");
        Page<ItemType> page = itemTypeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /item-types/:id} : get the "id" itemType.
     *
     * @param id the id of the itemType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the itemType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/item-types/{id}")
    public ResponseEntity<ItemType> getItemType(@PathVariable Long id) {
        log.debug("REST request to get ItemType : {}", id);
        Optional<ItemType> itemType = itemTypeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(itemType);
    }

    /**
     * {@code DELETE  /item-types/:id} : delete the "id" itemType.
     *
     * @param id the id of the itemType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/item-types/{id}")
    public ResponseEntity<Void> deleteItemType(@PathVariable Long id) {
        log.debug("REST request to delete ItemType : {}", id);
        itemTypeRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
