package edu.mum.life.web.rest;

import edu.mum.life.MumLifeApp;
import edu.mum.life.domain.ReservationRecord;
import edu.mum.life.repository.ReservationRecordRepository;
import edu.mum.life.service.ReservationRecordService;
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

/**
 * Integration tests for the {@link ReservationRecordResource} REST controller.
 */
@SpringBootTest(classes = MumLifeApp.class)
public class ReservationRecordResourceIT {

    private static final ZonedDateTime DEFAULT_EXPIRATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_EXPIRATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_EXPIRATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    @Autowired
    private ReservationRecordRepository reservationRecordRepository;

    @Autowired
    private ReservationRecordService reservationRecordService;

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

    private MockMvc restReservationRecordMockMvc;

    private ReservationRecord reservationRecord;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ReservationRecordResource reservationRecordResource = new ReservationRecordResource(reservationRecordService);
        this.restReservationRecordMockMvc = MockMvcBuilders.standaloneSetup(reservationRecordResource)
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
    public static ReservationRecord createEntity(EntityManager em) {
        ReservationRecord reservationRecord = new ReservationRecord()
            .expirationDate(DEFAULT_EXPIRATION_DATE)
            .createdAt(DEFAULT_CREATED_AT);
        return reservationRecord;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReservationRecord createUpdatedEntity(EntityManager em) {
        ReservationRecord reservationRecord = new ReservationRecord()
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .createdAt(UPDATED_CREATED_AT);
        return reservationRecord;
    }

    @BeforeEach
    public void initTest() {
        reservationRecord = createEntity(em);
    }

    @Test
    @Transactional
    public void createReservationRecord() throws Exception {
        int databaseSizeBeforeCreate = reservationRecordRepository.findAll().size();

        // Create the ReservationRecord
        restReservationRecordMockMvc.perform(post("/api/reservation-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reservationRecord)))
            .andExpect(status().isCreated());

        // Validate the ReservationRecord in the database
        List<ReservationRecord> reservationRecordList = reservationRecordRepository.findAll();
        assertThat(reservationRecordList).hasSize(databaseSizeBeforeCreate + 1);
        ReservationRecord testReservationRecord = reservationRecordList.get(reservationRecordList.size() - 1);
        assertThat(testReservationRecord.getExpirationDate()).isEqualTo(DEFAULT_EXPIRATION_DATE);
        assertThat(testReservationRecord.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    public void createReservationRecordWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = reservationRecordRepository.findAll().size();

        // Create the ReservationRecord with an existing ID
        reservationRecord.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReservationRecordMockMvc.perform(post("/api/reservation-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reservationRecord)))
            .andExpect(status().isBadRequest());

        // Validate the ReservationRecord in the database
        List<ReservationRecord> reservationRecordList = reservationRecordRepository.findAll();
        assertThat(reservationRecordList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkExpirationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = reservationRecordRepository.findAll().size();
        // set the field null
        reservationRecord.setExpirationDate(null);

        // Create the ReservationRecord, which fails.

        restReservationRecordMockMvc.perform(post("/api/reservation-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reservationRecord)))
            .andExpect(status().isBadRequest());

        List<ReservationRecord> reservationRecordList = reservationRecordRepository.findAll();
        assertThat(reservationRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = reservationRecordRepository.findAll().size();
        // set the field null
        reservationRecord.setCreatedAt(null);

        // Create the ReservationRecord, which fails.

        restReservationRecordMockMvc.perform(post("/api/reservation-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reservationRecord)))
            .andExpect(status().isBadRequest());

        List<ReservationRecord> reservationRecordList = reservationRecordRepository.findAll();
        assertThat(reservationRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllReservationRecords() throws Exception {
        // Initialize the database
        reservationRecordRepository.saveAndFlush(reservationRecord);

        // Get all the reservationRecordList
        restReservationRecordMockMvc.perform(get("/api/reservation-records?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reservationRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(sameInstant(DEFAULT_EXPIRATION_DATE))))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))));
    }
    
    @Test
    @Transactional
    public void getReservationRecord() throws Exception {
        // Initialize the database
        reservationRecordRepository.saveAndFlush(reservationRecord);

        // Get the reservationRecord
        restReservationRecordMockMvc.perform(get("/api/reservation-records/{id}", reservationRecord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(reservationRecord.getId().intValue()))
            .andExpect(jsonPath("$.expirationDate").value(sameInstant(DEFAULT_EXPIRATION_DATE)))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)));
    }

    @Test
    @Transactional
    public void getNonExistingReservationRecord() throws Exception {
        // Get the reservationRecord
        restReservationRecordMockMvc.perform(get("/api/reservation-records/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReservationRecord() throws Exception {
        // Initialize the database
        reservationRecordService.save(reservationRecord);

        int databaseSizeBeforeUpdate = reservationRecordRepository.findAll().size();

        // Update the reservationRecord
        ReservationRecord updatedReservationRecord = reservationRecordRepository.findById(reservationRecord.getId()).get();
        // Disconnect from session so that the updates on updatedReservationRecord are not directly saved in db
        em.detach(updatedReservationRecord);
        updatedReservationRecord
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .createdAt(UPDATED_CREATED_AT);

        restReservationRecordMockMvc.perform(put("/api/reservation-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedReservationRecord)))
            .andExpect(status().isOk());

        // Validate the ReservationRecord in the database
        List<ReservationRecord> reservationRecordList = reservationRecordRepository.findAll();
        assertThat(reservationRecordList).hasSize(databaseSizeBeforeUpdate);
        ReservationRecord testReservationRecord = reservationRecordList.get(reservationRecordList.size() - 1);
        assertThat(testReservationRecord.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testReservationRecord.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingReservationRecord() throws Exception {
        int databaseSizeBeforeUpdate = reservationRecordRepository.findAll().size();

        // Create the ReservationRecord

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReservationRecordMockMvc.perform(put("/api/reservation-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reservationRecord)))
            .andExpect(status().isBadRequest());

        // Validate the ReservationRecord in the database
        List<ReservationRecord> reservationRecordList = reservationRecordRepository.findAll();
        assertThat(reservationRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteReservationRecord() throws Exception {
        // Initialize the database
        reservationRecordService.save(reservationRecord);

        int databaseSizeBeforeDelete = reservationRecordRepository.findAll().size();

        // Delete the reservationRecord
        restReservationRecordMockMvc.perform(delete("/api/reservation-records/{id}", reservationRecord.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ReservationRecord> reservationRecordList = reservationRecordRepository.findAll();
        assertThat(reservationRecordList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReservationRecord.class);
        ReservationRecord reservationRecord1 = new ReservationRecord();
        reservationRecord1.setId(1L);
        ReservationRecord reservationRecord2 = new ReservationRecord();
        reservationRecord2.setId(reservationRecord1.getId());
        assertThat(reservationRecord1).isEqualTo(reservationRecord2);
        reservationRecord2.setId(2L);
        assertThat(reservationRecord1).isNotEqualTo(reservationRecord2);
        reservationRecord1.setId(null);
        assertThat(reservationRecord1).isNotEqualTo(reservationRecord2);
    }
}
