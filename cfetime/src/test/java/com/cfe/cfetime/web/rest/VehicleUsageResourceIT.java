package com.cfe.cfetime.web.rest;

import static com.cfe.cfetime.domain.VehicleUsageAsserts.*;
import static com.cfe.cfetime.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cfe.cfetime.IntegrationTest;
import com.cfe.cfetime.domain.Employee;
import com.cfe.cfetime.domain.Status;
import com.cfe.cfetime.domain.Vehicle;
import com.cfe.cfetime.domain.VehicleUsage;
import com.cfe.cfetime.repository.VehicleUsageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link VehicleUsageResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class VehicleUsageResourceIT {

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/vehicle-usages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VehicleUsageRepository vehicleUsageRepository;

    @Mock
    private VehicleUsageRepository vehicleUsageRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVehicleUsageMockMvc;

    private VehicleUsage vehicleUsage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VehicleUsage createEntity(EntityManager em) {
        VehicleUsage vehicleUsage = new VehicleUsage().startDate(DEFAULT_START_DATE).endDate(DEFAULT_END_DATE).comments(DEFAULT_COMMENTS);
        // Add required entity
        Vehicle vehicle;
        if (TestUtil.findAll(em, Vehicle.class).isEmpty()) {
            vehicle = VehicleResourceIT.createEntity(em);
            em.persist(vehicle);
            em.flush();
        } else {
            vehicle = TestUtil.findAll(em, Vehicle.class).get(0);
        }
        vehicleUsage.setVehicle(vehicle);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        vehicleUsage.setEmployee(employee);
        // Add required entity
        Status status;
        if (TestUtil.findAll(em, Status.class).isEmpty()) {
            status = StatusResourceIT.createEntity(em);
            em.persist(status);
            em.flush();
        } else {
            status = TestUtil.findAll(em, Status.class).get(0);
        }
        vehicleUsage.setStatus(status);
        return vehicleUsage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VehicleUsage createUpdatedEntity(EntityManager em) {
        VehicleUsage vehicleUsage = new VehicleUsage().startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE).comments(UPDATED_COMMENTS);
        // Add required entity
        Vehicle vehicle;
        if (TestUtil.findAll(em, Vehicle.class).isEmpty()) {
            vehicle = VehicleResourceIT.createUpdatedEntity(em);
            em.persist(vehicle);
            em.flush();
        } else {
            vehicle = TestUtil.findAll(em, Vehicle.class).get(0);
        }
        vehicleUsage.setVehicle(vehicle);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createUpdatedEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        vehicleUsage.setEmployee(employee);
        // Add required entity
        Status status;
        if (TestUtil.findAll(em, Status.class).isEmpty()) {
            status = StatusResourceIT.createUpdatedEntity(em);
            em.persist(status);
            em.flush();
        } else {
            status = TestUtil.findAll(em, Status.class).get(0);
        }
        vehicleUsage.setStatus(status);
        return vehicleUsage;
    }

    @BeforeEach
    public void initTest() {
        vehicleUsage = createEntity(em);
    }

    @Test
    @Transactional
    void createVehicleUsage() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the VehicleUsage
        var returnedVehicleUsage = om.readValue(
            restVehicleUsageMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleUsage)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            VehicleUsage.class
        );

        // Validate the VehicleUsage in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertVehicleUsageUpdatableFieldsEquals(returnedVehicleUsage, getPersistedVehicleUsage(returnedVehicleUsage));
    }

    @Test
    @Transactional
    void createVehicleUsageWithExistingId() throws Exception {
        // Create the VehicleUsage with an existing ID
        vehicleUsage.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVehicleUsageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleUsage)))
            .andExpect(status().isBadRequest());

        // Validate the VehicleUsage in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vehicleUsage.setStartDate(null);

        // Create the VehicleUsage, which fails.

        restVehicleUsageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleUsage)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVehicleUsages() throws Exception {
        // Initialize the database
        vehicleUsageRepository.saveAndFlush(vehicleUsage);

        // Get all the vehicleUsageList
        restVehicleUsageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vehicleUsage.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVehicleUsagesWithEagerRelationshipsIsEnabled() throws Exception {
        when(vehicleUsageRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVehicleUsageMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(vehicleUsageRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVehicleUsagesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(vehicleUsageRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVehicleUsageMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(vehicleUsageRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getVehicleUsage() throws Exception {
        // Initialize the database
        vehicleUsageRepository.saveAndFlush(vehicleUsage);

        // Get the vehicleUsage
        restVehicleUsageMockMvc
            .perform(get(ENTITY_API_URL_ID, vehicleUsage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vehicleUsage.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS));
    }

    @Test
    @Transactional
    void getNonExistingVehicleUsage() throws Exception {
        // Get the vehicleUsage
        restVehicleUsageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVehicleUsage() throws Exception {
        // Initialize the database
        vehicleUsageRepository.saveAndFlush(vehicleUsage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicleUsage
        VehicleUsage updatedVehicleUsage = vehicleUsageRepository.findById(vehicleUsage.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVehicleUsage are not directly saved in db
        em.detach(updatedVehicleUsage);
        updatedVehicleUsage.startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE).comments(UPDATED_COMMENTS);

        restVehicleUsageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVehicleUsage.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedVehicleUsage))
            )
            .andExpect(status().isOk());

        // Validate the VehicleUsage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVehicleUsageToMatchAllProperties(updatedVehicleUsage);
    }

    @Test
    @Transactional
    void putNonExistingVehicleUsage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleUsage.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicleUsageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vehicleUsage.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vehicleUsage))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleUsage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVehicleUsage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleUsage.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleUsageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vehicleUsage))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleUsage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVehicleUsage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleUsage.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleUsageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleUsage)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VehicleUsage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVehicleUsageWithPatch() throws Exception {
        // Initialize the database
        vehicleUsageRepository.saveAndFlush(vehicleUsage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicleUsage using partial update
        VehicleUsage partialUpdatedVehicleUsage = new VehicleUsage();
        partialUpdatedVehicleUsage.setId(vehicleUsage.getId());

        partialUpdatedVehicleUsage.startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE);

        restVehicleUsageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicleUsage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVehicleUsage))
            )
            .andExpect(status().isOk());

        // Validate the VehicleUsage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVehicleUsageUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedVehicleUsage, vehicleUsage),
            getPersistedVehicleUsage(vehicleUsage)
        );
    }

    @Test
    @Transactional
    void fullUpdateVehicleUsageWithPatch() throws Exception {
        // Initialize the database
        vehicleUsageRepository.saveAndFlush(vehicleUsage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicleUsage using partial update
        VehicleUsage partialUpdatedVehicleUsage = new VehicleUsage();
        partialUpdatedVehicleUsage.setId(vehicleUsage.getId());

        partialUpdatedVehicleUsage.startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE).comments(UPDATED_COMMENTS);

        restVehicleUsageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicleUsage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVehicleUsage))
            )
            .andExpect(status().isOk());

        // Validate the VehicleUsage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVehicleUsageUpdatableFieldsEquals(partialUpdatedVehicleUsage, getPersistedVehicleUsage(partialUpdatedVehicleUsage));
    }

    @Test
    @Transactional
    void patchNonExistingVehicleUsage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleUsage.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicleUsageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vehicleUsage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vehicleUsage))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleUsage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVehicleUsage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleUsage.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleUsageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vehicleUsage))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleUsage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVehicleUsage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleUsage.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleUsageMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(vehicleUsage)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VehicleUsage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVehicleUsage() throws Exception {
        // Initialize the database
        vehicleUsageRepository.saveAndFlush(vehicleUsage);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the vehicleUsage
        restVehicleUsageMockMvc
            .perform(delete(ENTITY_API_URL_ID, vehicleUsage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return vehicleUsageRepository.count();
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

    protected VehicleUsage getPersistedVehicleUsage(VehicleUsage vehicleUsage) {
        return vehicleUsageRepository.findById(vehicleUsage.getId()).orElseThrow();
    }

    protected void assertPersistedVehicleUsageToMatchAllProperties(VehicleUsage expectedVehicleUsage) {
        assertVehicleUsageAllPropertiesEquals(expectedVehicleUsage, getPersistedVehicleUsage(expectedVehicleUsage));
    }

    protected void assertPersistedVehicleUsageToMatchUpdatableProperties(VehicleUsage expectedVehicleUsage) {
        assertVehicleUsageAllUpdatablePropertiesEquals(expectedVehicleUsage, getPersistedVehicleUsage(expectedVehicleUsage));
    }
}
