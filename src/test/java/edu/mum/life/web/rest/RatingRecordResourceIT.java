package edu.mum.life.web.rest;

import edu.mum.life.MumLifeApp;
import edu.mum.life.domain.RatingRecord;
import edu.mum.life.repository.RatingRecordRepository;
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
import org.springframework.util.Base64Utils;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static edu.mum.life.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link RatingRecordResource} REST controller.
 */
@SpringBootTest(classes = MumLifeApp.class)
public class RatingRecordResourceIT {

    private static final Integer DEFAULT_RATE_VALUE = 1;
    private static final Integer UPDATED_RATE_VALUE = 2;
    private static final Integer SMALLER_RATE_VALUE = 1 - 1;

    private static final byte[] DEFAULT_COMMENT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_COMMENT = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_COMMENT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_COMMENT_CONTENT_TYPE = "image/png";

    @Autowired
    private RatingRecordRepository ratingRecordRepository;

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

    private MockMvc restRatingRecordMockMvc;

    private RatingRecord ratingRecord;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RatingRecordResource ratingRecordResource = new RatingRecordResource(ratingRecordRepository);
        this.restRatingRecordMockMvc = MockMvcBuilders.standaloneSetup(ratingRecordResource)
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
    public static RatingRecord createEntity(EntityManager em) {
        RatingRecord ratingRecord = new RatingRecord()
            .rateValue(DEFAULT_RATE_VALUE)
            .comment(DEFAULT_COMMENT)
            .commentContentType(DEFAULT_COMMENT_CONTENT_TYPE);
        return ratingRecord;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RatingRecord createUpdatedEntity(EntityManager em) {
        RatingRecord ratingRecord = new RatingRecord()
            .rateValue(UPDATED_RATE_VALUE)
            .comment(UPDATED_COMMENT)
            .commentContentType(UPDATED_COMMENT_CONTENT_TYPE);
        return ratingRecord;
    }

    @BeforeEach
    public void initTest() {
        ratingRecord = createEntity(em);
    }

    @Test
    @Transactional
    public void createRatingRecord() throws Exception {
        int databaseSizeBeforeCreate = ratingRecordRepository.findAll().size();

        // Create the RatingRecord
        restRatingRecordMockMvc.perform(post("/api/rating-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ratingRecord)))
            .andExpect(status().isCreated());

        // Validate the RatingRecord in the database
        List<RatingRecord> ratingRecordList = ratingRecordRepository.findAll();
        assertThat(ratingRecordList).hasSize(databaseSizeBeforeCreate + 1);
        RatingRecord testRatingRecord = ratingRecordList.get(ratingRecordList.size() - 1);
        assertThat(testRatingRecord.getRateValue()).isEqualTo(DEFAULT_RATE_VALUE);
        assertThat(testRatingRecord.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testRatingRecord.getCommentContentType()).isEqualTo(DEFAULT_COMMENT_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createRatingRecordWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ratingRecordRepository.findAll().size();

        // Create the RatingRecord with an existing ID
        ratingRecord.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRatingRecordMockMvc.perform(post("/api/rating-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ratingRecord)))
            .andExpect(status().isBadRequest());

        // Validate the RatingRecord in the database
        List<RatingRecord> ratingRecordList = ratingRecordRepository.findAll();
        assertThat(ratingRecordList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllRatingRecords() throws Exception {
        // Initialize the database
        ratingRecordRepository.saveAndFlush(ratingRecord);

        // Get all the ratingRecordList
        restRatingRecordMockMvc.perform(get("/api/rating-records?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ratingRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].rateValue").value(hasItem(DEFAULT_RATE_VALUE)))
            .andExpect(jsonPath("$.[*].commentContentType").value(hasItem(DEFAULT_COMMENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(Base64Utils.encodeToString(DEFAULT_COMMENT))));
    }
    
    @Test
    @Transactional
    public void getRatingRecord() throws Exception {
        // Initialize the database
        ratingRecordRepository.saveAndFlush(ratingRecord);

        // Get the ratingRecord
        restRatingRecordMockMvc.perform(get("/api/rating-records/{id}", ratingRecord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ratingRecord.getId().intValue()))
            .andExpect(jsonPath("$.rateValue").value(DEFAULT_RATE_VALUE))
            .andExpect(jsonPath("$.commentContentType").value(DEFAULT_COMMENT_CONTENT_TYPE))
            .andExpect(jsonPath("$.comment").value(Base64Utils.encodeToString(DEFAULT_COMMENT)));
    }

    @Test
    @Transactional
    public void getNonExistingRatingRecord() throws Exception {
        // Get the ratingRecord
        restRatingRecordMockMvc.perform(get("/api/rating-records/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRatingRecord() throws Exception {
        // Initialize the database
        ratingRecordRepository.saveAndFlush(ratingRecord);

        int databaseSizeBeforeUpdate = ratingRecordRepository.findAll().size();

        // Update the ratingRecord
        RatingRecord updatedRatingRecord = ratingRecordRepository.findById(ratingRecord.getId()).get();
        // Disconnect from session so that the updates on updatedRatingRecord are not directly saved in db
        em.detach(updatedRatingRecord);
        updatedRatingRecord
            .rateValue(UPDATED_RATE_VALUE)
            .comment(UPDATED_COMMENT)
            .commentContentType(UPDATED_COMMENT_CONTENT_TYPE);

        restRatingRecordMockMvc.perform(put("/api/rating-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRatingRecord)))
            .andExpect(status().isOk());

        // Validate the RatingRecord in the database
        List<RatingRecord> ratingRecordList = ratingRecordRepository.findAll();
        assertThat(ratingRecordList).hasSize(databaseSizeBeforeUpdate);
        RatingRecord testRatingRecord = ratingRecordList.get(ratingRecordList.size() - 1);
        assertThat(testRatingRecord.getRateValue()).isEqualTo(UPDATED_RATE_VALUE);
        assertThat(testRatingRecord.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testRatingRecord.getCommentContentType()).isEqualTo(UPDATED_COMMENT_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingRatingRecord() throws Exception {
        int databaseSizeBeforeUpdate = ratingRecordRepository.findAll().size();

        // Create the RatingRecord

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRatingRecordMockMvc.perform(put("/api/rating-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ratingRecord)))
            .andExpect(status().isBadRequest());

        // Validate the RatingRecord in the database
        List<RatingRecord> ratingRecordList = ratingRecordRepository.findAll();
        assertThat(ratingRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRatingRecord() throws Exception {
        // Initialize the database
        ratingRecordRepository.saveAndFlush(ratingRecord);

        int databaseSizeBeforeDelete = ratingRecordRepository.findAll().size();

        // Delete the ratingRecord
        restRatingRecordMockMvc.perform(delete("/api/rating-records/{id}", ratingRecord.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RatingRecord> ratingRecordList = ratingRecordRepository.findAll();
        assertThat(ratingRecordList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RatingRecord.class);
        RatingRecord ratingRecord1 = new RatingRecord();
        ratingRecord1.setId(1L);
        RatingRecord ratingRecord2 = new RatingRecord();
        ratingRecord2.setId(ratingRecord1.getId());
        assertThat(ratingRecord1).isEqualTo(ratingRecord2);
        ratingRecord2.setId(2L);
        assertThat(ratingRecord1).isNotEqualTo(ratingRecord2);
        ratingRecord1.setId(null);
        assertThat(ratingRecord1).isNotEqualTo(ratingRecord2);
    }
}
