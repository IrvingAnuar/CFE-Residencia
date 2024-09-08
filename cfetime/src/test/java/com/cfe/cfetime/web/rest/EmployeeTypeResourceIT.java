package com.cfe.cfetime.web.rest;

import static com.cfe.cfetime.domain.EmployeeTypeAsserts.*;
import static com.cfe.cfetime.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cfe.cfetime.IntegrationTest;
import com.cfe.cfetime.domain.EmployeeType;
import com.cfe.cfetime.repository.EmployeeTypeRepository;
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
 * Integration tests for the {@link EmployeeTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmployeeTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/employee-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EmployeeTypeRepository employeeTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmployeeTypeMockMvc;

    private EmployeeType employeeType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployeeType createEntity(EntityManager em) {
        EmployeeType employeeType = new EmployeeType().name(DEFAULT_NAME);
        return employeeType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployeeType createUpdatedEntity(EntityManager em) {
        EmployeeType employeeType = new EmployeeType().name(UPDATED_NAME);
        return employeeType;
    }

    @BeforeEach
    public void initTest() {
        employeeType = createEntity(em);
    }

    @Test
    @Transactional
    void createEmployeeType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EmployeeType
        var returnedEmployeeType = om.readValue(
            restEmployeeTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EmployeeType.class
        );

        // Validate the EmployeeType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertEmployeeTypeUpdatableFieldsEquals(returnedEmployeeType, getPersistedEmployeeType(returnedEmployeeType));
    }

    @Test
    @Transactional
    void createEmployeeTypeWithExistingId() throws Exception {
        // Create the EmployeeType with an existing ID
        employeeType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeeTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeType)))
            .andExpect(status().isBadRequest());

        // Validate the EmployeeType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEmployeeTypes() throws Exception {
        // Initialize the database
        employeeTypeRepository.saveAndFlush(employeeType);

        // Get all the employeeTypeList
        restEmployeeTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employeeType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getEmployeeType() throws Exception {
        // Initialize the database
        employeeTypeRepository.saveAndFlush(employeeType);

        // Get the employeeType
        restEmployeeTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, employeeType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employeeType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingEmployeeType() throws Exception {
        // Get the employeeType
        restEmployeeTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmployeeType() throws Exception {
        // Initialize the database
        employeeTypeRepository.saveAndFlush(employeeType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employeeType
        EmployeeType updatedEmployeeType = employeeTypeRepository.findById(employeeType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEmployeeType are not directly saved in db
        em.detach(updatedEmployeeType);
        updatedEmployeeType.name(UPDATED_NAME);

        restEmployeeTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEmployeeType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedEmployeeType))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEmployeeTypeToMatchAllProperties(updatedEmployeeType);
    }

    @Test
    @Transactional
    void putNonExistingEmployeeType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeeType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(employeeType))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmployeeType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(employeeType))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmployeeType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeeType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmployeeTypeWithPatch() throws Exception {
        // Initialize the database
        employeeTypeRepository.saveAndFlush(employeeType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employeeType using partial update
        EmployeeType partialUpdatedEmployeeType = new EmployeeType();
        partialUpdatedEmployeeType.setId(employeeType.getId());

        partialUpdatedEmployeeType.name(UPDATED_NAME);

        restEmployeeTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeeType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmployeeType))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmployeeTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEmployeeType, employeeType),
            getPersistedEmployeeType(employeeType)
        );
    }

    @Test
    @Transactional
    void fullUpdateEmployeeTypeWithPatch() throws Exception {
        // Initialize the database
        employeeTypeRepository.saveAndFlush(employeeType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employeeType using partial update
        EmployeeType partialUpdatedEmployeeType = new EmployeeType();
        partialUpdatedEmployeeType.setId(employeeType.getId());

        partialUpdatedEmployeeType.name(UPDATED_NAME);

        restEmployeeTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeeType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmployeeType))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmployeeTypeUpdatableFieldsEquals(partialUpdatedEmployeeType, getPersistedEmployeeType(partialUpdatedEmployeeType));
    }

    @Test
    @Transactional
    void patchNonExistingEmployeeType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, employeeType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(employeeType))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmployeeType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(employeeType))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmployeeType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(employeeType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeeType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmployeeType() throws Exception {
        // Initialize the database
        employeeTypeRepository.saveAndFlush(employeeType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the employeeType
        restEmployeeTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, employeeType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return employeeTypeRepository.count();
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

    protected EmployeeType getPersistedEmployeeType(EmployeeType employeeType) {
        return employeeTypeRepository.findById(employeeType.getId()).orElseThrow();
    }

    protected void assertPersistedEmployeeTypeToMatchAllProperties(EmployeeType expectedEmployeeType) {
        assertEmployeeTypeAllPropertiesEquals(expectedEmployeeType, getPersistedEmployeeType(expectedEmployeeType));
    }

    protected void assertPersistedEmployeeTypeToMatchUpdatableProperties(EmployeeType expectedEmployeeType) {
        assertEmployeeTypeAllUpdatablePropertiesEquals(expectedEmployeeType, getPersistedEmployeeType(expectedEmployeeType));
    }
}
