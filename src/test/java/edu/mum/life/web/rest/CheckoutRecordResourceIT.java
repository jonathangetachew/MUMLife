package edu.mum.life.web.rest;

import edu.mum.life.MumLifeApp;
import edu.mum.life.domain.CheckoutRecord;
import edu.mum.life.repository.CheckoutRecordRepository;
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
 * Integration tests for the {@link CheckoutRecordResource} REST controller.
 */
@SpringBootTest(classes = MumLifeApp.class)
public class CheckoutRecordResourceIT {

    private static final ZonedDateTime DEFAULT_DUE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DUE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DUE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    @Autowired
    private CheckoutRecordRepository checkoutRecordRepository;

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

    private MockMvc restCheckoutRecordMockMvc;

    private CheckoutRecord checkoutRecord;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CheckoutRecordResource checkoutRecordResource = new CheckoutRecordResource(checkoutRecordRepository);
        this.restCheckoutRecordMockMvc = MockMvcBuilders.standaloneSetup(checkoutRecordResource)
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
    public static CheckoutRecord createEntity(EntityManager em) {
        CheckoutRecord checkoutRecord = new CheckoutRecord()
            .dueDate(DEFAULT_DUE_DATE)
            .createdAt(DEFAULT_CREATED_AT);
        return checkoutRecord;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CheckoutRecord createUpdatedEntity(EntityManager em) {
        CheckoutRecord checkoutRecord = new CheckoutRecord()
            .dueDate(UPDATED_DUE_DATE)
            .createdAt(UPDATED_CREATED_AT);
        return checkoutRecord;
    }

    @BeforeEach
    public void initTest() {
        checkoutRecord = createEntity(em);
    }

    @Test
    @Transactional
    public void createCheckoutRecord() throws Exception {
        int databaseSizeBeforeCreate = checkoutRecordRepository.findAll().size();

        // Create the CheckoutRecord
        restCheckoutRecordMockMvc.perform(post("/api/checkout-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(checkoutRecord)))
            .andExpect(status().isCreated());

        // Validate the CheckoutRecord in the database
        List<CheckoutRecord> checkoutRecordList = checkoutRecordRepository.findAll();
        assertThat(checkoutRecordList).hasSize(databaseSizeBeforeCreate + 1);
        CheckoutRecord testCheckoutRecord = checkoutRecordList.get(checkoutRecordList.size() - 1);
        assertThat(testCheckoutRecord.getDueDate()).isEqualTo(DEFAULT_DUE_DATE);
        assertThat(testCheckoutRecord.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    public void createCheckoutRecordWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = checkoutRecordRepository.findAll().size();

        // Create the CheckoutRecord with an existing ID
        checkoutRecord.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCheckoutRecordMockMvc.perform(post("/api/checkout-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(checkoutRecord)))
            .andExpect(status().isBadRequest());

        // Validate the CheckoutRecord in the database
        List<CheckoutRecord> checkoutRecordList = checkoutRecordRepository.findAll();
        assertThat(checkoutRecordList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkDueDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = checkoutRecordRepository.findAll().size();
        // set the field null
        checkoutRecord.setDueDate(null);

        // Create the CheckoutRecord, which fails.

        restCheckoutRecordMockMvc.perform(post("/api/checkout-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(checkoutRecord)))
            .andExpect(status().isBadRequest());

        List<CheckoutRecord> checkoutRecordList = checkoutRecordRepository.findAll();
        assertThat(checkoutRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = checkoutRecordRepository.findAll().size();
        // set the field null
        checkoutRecord.setCreatedAt(null);

        // Create the CheckoutRecord, which fails.

        restCheckoutRecordMockMvc.perform(post("/api/checkout-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(checkoutRecord)))
            .andExpect(status().isBadRequest());

        List<CheckoutRecord> checkoutRecordList = checkoutRecordRepository.findAll();
        assertThat(checkoutRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCheckoutRecords() throws Exception {
        // Initialize the database
        checkoutRecordRepository.saveAndFlush(checkoutRecord);

        // Get all the checkoutRecordList
        restCheckoutRecordMockMvc.perform(get("/api/checkout-records?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(checkoutRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(sameInstant(DEFAULT_DUE_DATE))))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))));
    }
    
    @Test
    @Transactional
    public void getCheckoutRecord() throws Exception {
        // Initialize the database
        checkoutRecordRepository.saveAndFlush(checkoutRecord);

        // Get the checkoutRecord
        restCheckoutRecordMockMvc.perform(get("/api/checkout-records/{id}", checkoutRecord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(checkoutRecord.getId().intValue()))
            .andExpect(jsonPath("$.dueDate").value(sameInstant(DEFAULT_DUE_DATE)))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)));
    }

    @Test
    @Transactional
    public void getNonExistingCheckoutRecord() throws Exception {
        // Get the checkoutRecord
        restCheckoutRecordMockMvc.perform(get("/api/checkout-records/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCheckoutRecord() throws Exception {
        // Initialize the database
        checkoutRecordRepository.saveAndFlush(checkoutRecord);

        int databaseSizeBeforeUpdate = checkoutRecordRepository.findAll().size();

        // Update the checkoutRecord
        CheckoutRecord updatedCheckoutRecord = checkoutRecordRepository.findById(checkoutRecord.getId()).get();
        // Disconnect from session so that the updates on updatedCheckoutRecord are not directly saved in db
        em.detach(updatedCheckoutRecord);
        updatedCheckoutRecord
            .dueDate(UPDATED_DUE_DATE)
            .createdAt(UPDATED_CREATED_AT);

        restCheckoutRecordMockMvc.perform(put("/api/checkout-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCheckoutRecord)))
            .andExpect(status().isOk());

        // Validate the CheckoutRecord in the database
        List<CheckoutRecord> checkoutRecordList = checkoutRecordRepository.findAll();
        assertThat(checkoutRecordList).hasSize(databaseSizeBeforeUpdate);
        CheckoutRecord testCheckoutRecord = checkoutRecordList.get(checkoutRecordList.size() - 1);
        assertThat(testCheckoutRecord.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
        assertThat(testCheckoutRecord.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingCheckoutRecord() throws Exception {
        int databaseSizeBeforeUpdate = checkoutRecordRepository.findAll().size();

        // Create the CheckoutRecord

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCheckoutRecordMockMvc.perform(put("/api/checkout-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(checkoutRecord)))
            .andExpect(status().isBadRequest());

        // Validate the CheckoutRecord in the database
        List<CheckoutRecord> checkoutRecordList = checkoutRecordRepository.findAll();
        assertThat(checkoutRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCheckoutRecord() throws Exception {
        // Initialize the database
        checkoutRecordRepository.saveAndFlush(checkoutRecord);

        int databaseSizeBeforeDelete = checkoutRecordRepository.findAll().size();

        // Delete the checkoutRecord
        restCheckoutRecordMockMvc.perform(delete("/api/checkout-records/{id}", checkoutRecord.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CheckoutRecord> checkoutRecordList = checkoutRecordRepository.findAll();
        assertThat(checkoutRecordList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CheckoutRecord.class);
        CheckoutRecord checkoutRecord1 = new CheckoutRecord();
        checkoutRecord1.setId(1L);
        CheckoutRecord checkoutRecord2 = new CheckoutRecord();
        checkoutRecord2.setId(checkoutRecord1.getId());
        assertThat(checkoutRecord1).isEqualTo(checkoutRecord2);
        checkoutRecord2.setId(2L);
        assertThat(checkoutRecord1).isNotEqualTo(checkoutRecord2);
        checkoutRecord1.setId(null);
        assertThat(checkoutRecord1).isNotEqualTo(checkoutRecord2);
    }
}
