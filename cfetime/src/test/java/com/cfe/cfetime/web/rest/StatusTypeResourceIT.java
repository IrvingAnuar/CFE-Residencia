package com.cfe.cfetime.web.rest;

import static com.cfe.cfetime.domain.StatusTypeAsserts.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cfe.cfetime.IntegrationTest;
import com.cfe.cfetime.domain.StatusType;
import com.cfe.cfetime.repository.StatusTypeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link StatusTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StatusTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/status-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private StatusTypeRepository statusTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStatusTypeMockMvc;

    private StatusType statusType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StatusType createEntity(EntityManager em) {
        StatusType statusType = new StatusType().name(DEFAULT_NAME);
        return statusType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StatusType createUpdatedEntity(EntityManager em) {
        StatusType statusType = new StatusType().name(UPDATED_NAME);
        return statusType;
    }

    @BeforeEach
    public void initTest() {
        statusType = createEntity(em);
    }

    @Test
    @Transactional
    void getAllStatusTypes() throws Exception {
        // Initialize the database
        statusTypeRepository.saveAndFlush(statusType);

        // Get all the statusTypeList
        restStatusTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(statusType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getStatusType() throws Exception {
        // Initialize the database
        statusTypeRepository.saveAndFlush(statusType);

        // Get the statusType
        restStatusTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, statusType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(statusType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingStatusType() throws Exception {
        // Get the statusType
        restStatusTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    protected long getRepositoryCount() {
        return statusTypeRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected StatusType getPersistedStatusType(StatusType statusType) {
        return statusTypeRepository.findById(statusType.getId()).orElseThrow();
    }

    protected void assertPersistedStatusTypeToMatchAllProperties(StatusType expectedStatusType) {
        assertStatusTypeAllPropertiesEquals(expectedStatusType, getPersistedStatusType(expectedStatusType));
    }

    protected void assertPersistedStatusTypeToMatchUpdatableProperties(StatusType expectedStatusType) {
        assertStatusTypeAllUpdatablePropertiesEquals(expectedStatusType, getPersistedStatusType(expectedStatusType));
    }
}
