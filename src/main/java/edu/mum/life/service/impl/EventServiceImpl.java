package edu.mum.life.service.impl;

import edu.mum.life.service.EventService;
import edu.mum.life.domain.Event;
import edu.mum.life.repository.EventRepository;
import edu.mum.life.repository.search.EventSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Event}.
 */
@Service
@Transactional
public class EventServiceImpl implements EventService {

    private final Logger log = LoggerFactory.getLogger(EventServiceImpl.class);

    private final EventRepository eventRepository;

    private final EventSearchRepository eventSearchRepository;

    public EventServiceImpl(EventRepository eventRepository, EventSearchRepository eventSearchRepository) {
        this.eventRepository = eventRepository;
        this.eventSearchRepository = eventSearchRepository;
    }

    /**
     * Save a event.
     *
     * @param event the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Event save(Event event) {
        log.debug("Request to save Event : {}", event);
        Event result = eventRepository.save(event);
        eventSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the events.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Event> findAll(Pageable pageable) {
        log.debug("Request to get all Events");
        return eventRepository.findAll(pageable);
    }


    /**
     * Get one event by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Event> findOne(Long id) {
        log.debug("Request to get Event : {}", id);
        return eventRepository.findById(id);
    }

    /**
     * Delete the event by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Event : {}", id);
        eventRepository.deleteById(id);
        eventSearchRepository.deleteById(id);
    }

    /**
     * Search for the event corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Event> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Events for query {}", query);
        return eventSearchRepository.search(queryStringQuery(query), pageable);    }
}
