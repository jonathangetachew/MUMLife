package edu.mum.life.service;

import edu.mum.life.domain.Item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Item}.
 */
public interface ItemService {

    /**
     * Save a item.
     *
     * @param item the entity to save.
     * @return the persisted entity.
     */
    Item save(Item item);

    /**
     * Get all the items.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Item> findAll(Pageable pageable);

    /**
     *
     * Get all Available items
     *
     * @param pageable
     * @return
     */
    Page<Item> findAllAvailable(Pageable pageable);

    /**
     * Get the "id" item.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Item> findOne(Long id);

    /**
     * Delete the "id" item.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
