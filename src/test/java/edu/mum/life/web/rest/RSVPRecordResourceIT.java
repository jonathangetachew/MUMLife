package edu.mum.life.web.rest;

import edu.mum.life.MumLifeApp;
import edu.mum.life.domain.RSVPRecord;
import edu.mum.life.repository.RSVPRecordRepository;
import edu.mum.life.service.RSVPRecordService;
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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static edu.mum.life.web.rest.TestUtil.sameInstant;
import static edu.mum.life.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import edu.mum.life.domain.enumeration.RSVPType;
/**
 * Integration tests for the {@link RSVPRecordResource} REST controller.
 */
@SpringBootTest(classes = MumLifeApp.class)
public class RSVPRecordResourceIT {

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final RSVPType DEFAULT_STATUS = RSVPType.GOING;
    private static final RSVPType UPDATED_STATUS = RSVPType.INTERESTED;

    @Autowired
    private RSVPRecordRepository rSVPRecordRepository;

    @Autowired
    private RSVPRecordService rSVPRecordService;

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

    private MockMvc restRSVPRecordMockMvc;

    private RSVPRecord rSVPRecord;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RSVPRecordResource rSVPRecordResource = new RSVPRecordResource(rSVPRecordService);
        this.restRSVPRecordMockMvc = MockMvcBuilders.standaloneSetup(rSVPRecordResource)
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
    public static RSVPRecord createEntity(EntityManager em) {
        RSVPRecord rSVPRecord = new RSVPRecord()
            .createdAt(DEFAULT_CREATED_AT)
            .status(DEFAULT_STATUS);
        return rSVPRecord;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RSVPRecord createUpdatedEntity(EntityManager em) {
        RSVPRecord rSVPRecord = new RSVPRecord()
            .createdAt(UPDATED_CREATED_AT)
            .status(UPDATED_STATUS);
        return rSVPRecord;
    }

    @BeforeEach
    public void initTest() {
        rSVPRecord = createEntity(em);
    }

    @Test
    @Transactional
    public void createRSVPRecord() throws Exception {
        int databaseSizeBeforeCreate = rSVPRecordRepository.findAll().size();

        // Create the RSVPRecord
        restRSVPRecordMockMvc.perform(post("/api/rsvp-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rSVPRecord)))
            .andExpect(status().isCreated());

        // Validate the RSVPRecord in the database
        List<RSVPRecord> rSVPRecordList = rSVPRecordRepository.findAll();
        assertThat(rSVPRecordList).hasSize(databaseSizeBeforeCreate + 1);
        RSVPRecord testRSVPRecord = rSVPRecordList.get(rSVPRecordList.size() - 1);
        assertThat(testRSVPRecord.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testRSVPRecord.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createRSVPRecordWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rSVPRecordRepository.findAll().size();

        // Create the RSVPRecord with an existing ID
        rSVPRecord.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRSVPRecordMockMvc.perform(post("/api/rsvp-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rSVPRecord)))
            .andExpect(status().isBadRequest());

        // Validate the RSVPRecord in the database
        List<RSVPRecord> rSVPRecordList = rSVPRecordRepository.findAll();
        assertThat(rSVPRecordList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = rSVPRecordRepository.findAll().size();
        // set the field null
        rSVPRecord.setCreatedAt(null);

        // Create the RSVPRecord, which fails.

        restRSVPRecordMockMvc.perform(post("/api/rsvp-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rSVPRecord)))
            .andExpect(status().isBadRequest());

        List<RSVPRecord> rSVPRecordList = rSVPRecordRepository.findAll();
        assertThat(rSVPRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = rSVPRecordRepository.findAll().size();
        // set the field null
        rSVPRecord.setStatus(null);

        // Create the RSVPRecord, which fails.

        restRSVPRecordMockMvc.perform(post("/api/rsvp-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rSVPRecord)))
            .andExpect(status().isBadRequest());

        List<RSVPRecord> rSVPRecordList = rSVPRecordRepository.findAll();
        assertThat(rSVPRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRSVPRecords() throws Exception {
        // Initialize the database
        rSVPRecordRepository.saveAndFlush(rSVPRecord);

        // Get all the rSVPRecordList
        restRSVPRecordMockMvc.perform(get("/api/rsvp-records?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rSVPRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
    
    @Test
    @Transactional
    public void getRSVPRecord() throws Exception {
        // Initialize the database
        rSVPRecordRepository.saveAndFlush(rSVPRecord);

        // Get the rSVPRecord
        restRSVPRecordMockMvc.perform(get("/api/rsvp-records/{id}", rSVPRecord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(rSVPRecord.getId().intValue()))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRSVPRecord() throws Exception {
        // Get the rSVPRecord
        restRSVPRecordMockMvc.perform(get("/api/rsvp-records/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRSVPRecord() throws Exception {
        // Initialize the database
        rSVPRecordService.save(rSVPRecord);

        int databaseSizeBeforeUpdate = rSVPRecordRepository.findAll().size();

        // Update the rSVPRecord
        RSVPRecord updatedRSVPRecord = rSVPRecordRepository.findById(rSVPRecord.getId()).get();
        // Disconnect from session so that the updates on updatedRSVPRecord are not directly saved in db
        em.detach(updatedRSVPRecord);
        updatedRSVPRecord
            .createdAt(UPDATED_CREATED_AT)
            .status(UPDATED_STATUS);

        restRSVPRecordMockMvc.perform(put("/api/rsvp-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRSVPRecord)))
            .andExpect(status().isOk());

        // Validate the RSVPRecord in the database
        List<RSVPRecord> rSVPRecordList = rSVPRecordRepository.findAll();
        assertThat(rSVPRecordList).hasSize(databaseSizeBeforeUpdate);
        RSVPRecord testRSVPRecord = rSVPRecordList.get(rSVPRecordList.size() - 1);
        assertThat(testRSVPRecord.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testRSVPRecord.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingRSVPRecord() throws Exception {
        int databaseSizeBeforeUpdate = rSVPRecordRepository.findAll().size();

        // Create the RSVPRecord

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRSVPRecordMockMvc.perform(put("/api/rsvp-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rSVPRecord)))
            .andExpect(status().isBadRequest());

        // Validate the RSVPRecord in the database
        List<RSVPRecord> rSVPRecordList = rSVPRecordRepository.findAll();
        assertThat(rSVPRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRSVPRecord() throws Exception {
        // Initialize the database
        rSVPRecordService.save(rSVPRecord);

        int databaseSizeBeforeDelete = rSVPRecordRepository.findAll().size();

        // Delete the rSVPRecord
        restRSVPRecordMockMvc.perform(delete("/api/rsvp-records/{id}", rSVPRecord.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RSVPRecord> rSVPRecordList = rSVPRecordRepository.findAll();
        assertThat(rSVPRecordList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RSVPRecord.class);
        RSVPRecord rSVPRecord1 = new RSVPRecord();
        rSVPRecord1.setId(1L);
        RSVPRecord rSVPRecord2 = new RSVPRecord();
        rSVPRecord2.setId(rSVPRecord1.getId());
        assertThat(rSVPRecord1).isEqualTo(rSVPRecord2);
        rSVPRecord2.setId(2L);
        assertThat(rSVPRecord1).isNotEqualTo(rSVPRecord2);
        rSVPRecord1.setId(null);
        assertThat(rSVPRecord1).isNotEqualTo(rSVPRecord2);
    }
}
