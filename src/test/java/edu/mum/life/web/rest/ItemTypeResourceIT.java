package edu.mum.life.web.rest;

import edu.mum.life.MumLifeApp;
import edu.mum.life.domain.ItemType;
import edu.mum.life.repository.ItemTypeRepository;
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
 * Integration tests for the {@link ItemTypeResource} REST controller.
 */
@SpringBootTest(classes = MumLifeApp.class)
public class ItemTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private ItemTypeRepository itemTypeRepository;

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

    private MockMvc restItemTypeMockMvc;

    private ItemType itemType;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ItemTypeResource itemTypeResource = new ItemTypeResource(itemTypeRepository);
        this.restItemTypeMockMvc = MockMvcBuilders.standaloneSetup(itemTypeResource)
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
    public static ItemType createEntity(EntityManager em) {
        ItemType itemType = new ItemType()
            .name(DEFAULT_NAME);
        return itemType;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemType createUpdatedEntity(EntityManager em) {
        ItemType itemType = new ItemType()
            .name(UPDATED_NAME);
        return itemType;
    }

    @BeforeEach
    public void initTest() {
        itemType = createEntity(em);
    }

    @Test
    @Transactional
    public void createItemType() throws Exception {
        int databaseSizeBeforeCreate = itemTypeRepository.findAll().size();

        // Create the ItemType
        restItemTypeMockMvc.perform(post("/api/item-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemType)))
            .andExpect(status().isCreated());

        // Validate the ItemType in the database
        List<ItemType> itemTypeList = itemTypeRepository.findAll();
        assertThat(itemTypeList).hasSize(databaseSizeBeforeCreate + 1);
        ItemType testItemType = itemTypeList.get(itemTypeList.size() - 1);
        assertThat(testItemType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createItemTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = itemTypeRepository.findAll().size();

        // Create the ItemType with an existing ID
        itemType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemTypeMockMvc.perform(post("/api/item-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemType)))
            .andExpect(status().isBadRequest());

        // Validate the ItemType in the database
        List<ItemType> itemTypeList = itemTypeRepository.findAll();
        assertThat(itemTypeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemTypeRepository.findAll().size();
        // set the field null
        itemType.setName(null);

        // Create the ItemType, which fails.

        restItemTypeMockMvc.perform(post("/api/item-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemType)))
            .andExpect(status().isBadRequest());

        List<ItemType> itemTypeList = itemTypeRepository.findAll();
        assertThat(itemTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllItemTypes() throws Exception {
        // Initialize the database
        itemTypeRepository.saveAndFlush(itemType);

        // Get all the itemTypeList
        restItemTypeMockMvc.perform(get("/api/item-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getItemType() throws Exception {
        // Initialize the database
        itemTypeRepository.saveAndFlush(itemType);

        // Get the itemType
        restItemTypeMockMvc.perform(get("/api/item-types/{id}", itemType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(itemType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingItemType() throws Exception {
        // Get the itemType
        restItemTypeMockMvc.perform(get("/api/item-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateItemType() throws Exception {
        // Initialize the database
        itemTypeRepository.saveAndFlush(itemType);

        int databaseSizeBeforeUpdate = itemTypeRepository.findAll().size();

        // Update the itemType
        ItemType updatedItemType = itemTypeRepository.findById(itemType.getId()).get();
        // Disconnect from session so that the updates on updatedItemType are not directly saved in db
        em.detach(updatedItemType);
        updatedItemType
            .name(UPDATED_NAME);

        restItemTypeMockMvc.perform(put("/api/item-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedItemType)))
            .andExpect(status().isOk());

        // Validate the ItemType in the database
        List<ItemType> itemTypeList = itemTypeRepository.findAll();
        assertThat(itemTypeList).hasSize(databaseSizeBeforeUpdate);
        ItemType testItemType = itemTypeList.get(itemTypeList.size() - 1);
        assertThat(testItemType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingItemType() throws Exception {
        int databaseSizeBeforeUpdate = itemTypeRepository.findAll().size();

        // Create the ItemType

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemTypeMockMvc.perform(put("/api/item-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemType)))
            .andExpect(status().isBadRequest());

        // Validate the ItemType in the database
        List<ItemType> itemTypeList = itemTypeRepository.findAll();
        assertThat(itemTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteItemType() throws Exception {
        // Initialize the database
        itemTypeRepository.saveAndFlush(itemType);

        int databaseSizeBeforeDelete = itemTypeRepository.findAll().size();

        // Delete the itemType
        restItemTypeMockMvc.perform(delete("/api/item-types/{id}", itemType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ItemType> itemTypeList = itemTypeRepository.findAll();
        assertThat(itemTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemType.class);
        ItemType itemType1 = new ItemType();
        itemType1.setId(1L);
        ItemType itemType2 = new ItemType();
        itemType2.setId(itemType1.getId());
        assertThat(itemType1).isEqualTo(itemType2);
        itemType2.setId(2L);
        assertThat(itemType1).isNotEqualTo(itemType2);
        itemType1.setId(null);
        assertThat(itemType1).isNotEqualTo(itemType2);
    }
}
