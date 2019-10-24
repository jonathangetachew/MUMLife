package edu.mum.life.web.rest;

import edu.mum.life.MumLifeApp;
import edu.mum.life.domain.EventType;
import edu.mum.life.repository.EventTypeRepository;
import edu.mum.life.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static edu.mum.life.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link EventTypeResource} REST controller.
 */
@SpringBootTest(classes = MumLifeApp.class)
public class EventTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private EventTypeRepository eventTypeRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restEventTypeMockMvc;

    private EventType eventType;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EventTypeResource eventTypeResource = new EventTypeResource(eventTypeRepository);
        this.restEventTypeMockMvc = MockMvcBuilders.standaloneSetup(eventTypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventType createEntity(EntityManager em) {
        EventType eventType = new EventType()
            .name(DEFAULT_NAME);
        return eventType;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventType createUpdatedEntity(EntityManager em) {
        EventType eventType = new EventType()
            .name(UPDATED_NAME);
        return eventType;
    }

    @BeforeEach
    public void initTest() {
        eventType = createEntity(em);
    }

    @Test
    @Transactional
    public void createEventType() throws Exception {
        int databaseSizeBeforeCreate = eventTypeRepository.findAll().size();

        // Create the EventType
        restEventTypeMockMvc.perform(post("/api/event-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventType)))
            .andExpect(status().isCreated());

        // Validate the EventType in the database
        List<EventType> eventTypeList = eventTypeRepository.findAll();
        assertThat(eventTypeList).hasSize(databaseSizeBeforeCreate + 1);
        EventType testEventType = eventTypeList.get(eventTypeList.size() - 1);
        assertThat(testEventType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createEventTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = eventTypeRepository.findAll().size();

        // Create the EventType with an existing ID
        eventType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventTypeMockMvc.perform(post("/api/event-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventType)))
            .andExpect(status().isBadRequest());

        // Validate the EventType in the database
        List<EventType> eventTypeList = eventTypeRepository.findAll();
        assertThat(eventTypeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventTypeRepository.findAll().size();
        // set the field null
        eventType.setName(null);

        // Create the EventType, which fails.

        restEventTypeMockMvc.perform(post("/api/event-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventType)))
            .andExpect(status().isBadRequest());

        List<EventType> eventTypeList = eventTypeRepository.findAll();
        assertThat(eventTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEventTypes() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        // Get all the eventTypeList
        restEventTypeMockMvc.perform(get("/api/event-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getEventType() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        // Get the eventType
        restEventTypeMockMvc.perform(get("/api/event-types/{id}", eventType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(eventType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEventType() throws Exception {
        // Get the eventType
        restEventTypeMockMvc.perform(get("/api/event-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEventType() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        int databaseSizeBeforeUpdate = eventTypeRepository.findAll().size();

        // Update the eventType
        EventType updatedEventType = eventTypeRepository.findById(eventType.getId()).get();
        // Disconnect from session so that the updates on updatedEventType are not directly saved in db
        em.detach(updatedEventType);
        updatedEventType
            .name(UPDATED_NAME);

        restEventTypeMockMvc.perform(put("/api/event-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEventType)))
            .andExpect(status().isOk());

        // Validate the EventType in the database
        List<EventType> eventTypeList = eventTypeRepository.findAll();
        assertThat(eventTypeList).hasSize(databaseSizeBeforeUpdate);
        EventType testEventType = eventTypeList.get(eventTypeList.size() - 1);
        assertThat(testEventType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingEventType() throws Exception {
        int databaseSizeBeforeUpdate = eventTypeRepository.findAll().size();

        // Create the EventType

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventTypeMockMvc.perform(put("/api/event-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventType)))
            .andExpect(status().isBadRequest());

        // Validate the EventType in the database
        List<EventType> eventTypeList = eventTypeRepository.findAll();
        assertThat(eventTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEventType() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        int databaseSizeBeforeDelete = eventTypeRepository.findAll().size();

        // Delete the eventType
        restEventTypeMockMvc.perform(delete("/api/event-types/{id}", eventType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EventType> eventTypeList = eventTypeRepository.findAll();
        assertThat(eventTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventType.class);
        EventType eventType1 = new EventType();
        eventType1.setId(1L);
        EventType eventType2 = new EventType();
        eventType2.setId(eventType1.getId());
        assertThat(eventType1).isEqualTo(eventType2);
        eventType2.setId(2L);
        assertThat(eventType1).isNotEqualTo(eventType2);
        eventType1.setId(null);
        assertThat(eventType1).isNotEqualTo(eventType2);
    }
}
