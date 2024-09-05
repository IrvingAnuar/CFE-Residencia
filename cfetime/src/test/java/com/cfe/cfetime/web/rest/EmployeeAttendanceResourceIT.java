package com.cfe.cfetime.web.rest;

import static com.cfe.cfetime.domain.EmployeeAttendanceAsserts.*;
import static com.cfe.cfetime.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cfe.cfetime.IntegrationTest;
import com.cfe.cfetime.domain.Employee;
import com.cfe.cfetime.domain.EmployeeAttendance;
import com.cfe.cfetime.domain.Status;
import com.cfe.cfetime.repository.EmployeeAttendanceRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link EmployeeAttendanceResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EmployeeAttendanceResourceIT {

    private static final LocalDate DEFAULT_ATTENDANCE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ATTENDANCE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Instant DEFAULT_CHECK_IN_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CHECK_IN_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CHECK_OUT_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CHECK_OUT_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/employee-attendances";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EmployeeAttendanceRepository employeeAttendanceRepository;

    @Mock
    private EmployeeAttendanceRepository employeeAttendanceRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmployeeAttendanceMockMvc;

    private EmployeeAttendance employeeAttendance;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployeeAttendance createEntity(EntityManager em) {
        EmployeeAttendance employeeAttendance = new EmployeeAttendance()
            .attendanceDate(DEFAULT_ATTENDANCE_DATE)
            .checkInTime(DEFAULT_CHECK_IN_TIME)
            .checkOutTime(DEFAULT_CHECK_OUT_TIME)
            .comments(DEFAULT_COMMENTS);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        employeeAttendance.setEmployee(employee);
        // Add required entity
        Status status;
        if (TestUtil.findAll(em, Status.class).isEmpty()) {
            status = StatusResourceIT.createEntity(em);
            em.persist(status);
            em.flush();
        } else {
            status = TestUtil.findAll(em, Status.class).get(0);
        }
        employeeAttendance.setStatus(status);
        return employeeAttendance;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployeeAttendance createUpdatedEntity(EntityManager em) {
        EmployeeAttendance employeeAttendance = new EmployeeAttendance()
            .attendanceDate(UPDATED_ATTENDANCE_DATE)
            .checkInTime(UPDATED_CHECK_IN_TIME)
            .checkOutTime(UPDATED_CHECK_OUT_TIME)
            .comments(UPDATED_COMMENTS);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createUpdatedEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        employeeAttendance.setEmployee(employee);
        // Add required entity
        Status status;
        if (TestUtil.findAll(em, Status.class).isEmpty()) {
            status = StatusResourceIT.createUpdatedEntity(em);
            em.persist(status);
            em.flush();
        } else {
            status = TestUtil.findAll(em, Status.class).get(0);
        }
        employeeAttendance.setStatus(status);
        return employeeAttendance;
    }

    @BeforeEach
    public void initTest() {
        employeeAttendance = createEntity(em);
    }

    @Test
    @Transactional
    void createEmployeeAttendance() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EmployeeAttendance
        var returnedEmployeeAttendance = om.readValue(
            restEmployeeAttendanceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeAttendance)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EmployeeAttendance.class
        );

        // Validate the EmployeeAttendance in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertEmployeeAttendanceUpdatableFieldsEquals(
            returnedEmployeeAttendance,
            getPersistedEmployeeAttendance(returnedEmployeeAttendance)
        );
    }

    @Test
    @Transactional
    void createEmployeeAttendanceWithExistingId() throws Exception {
        // Create the EmployeeAttendance with an existing ID
        employeeAttendance.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeeAttendanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeAttendance)))
            .andExpect(status().isBadRequest());

        // Validate the EmployeeAttendance in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAttendanceDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        employeeAttendance.setAttendanceDate(null);

        // Create the EmployeeAttendance, which fails.

        restEmployeeAttendanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeAttendance)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCheckInTimeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        employeeAttendance.setCheckInTime(null);

        // Create the EmployeeAttendance, which fails.

        restEmployeeAttendanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeAttendance)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEmployeeAttendances() throws Exception {
        // Initialize the database
        employeeAttendanceRepository.saveAndFlush(employeeAttendance);

        // Get all the employeeAttendanceList
        restEmployeeAttendanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employeeAttendance.getId().intValue())))
            .andExpect(jsonPath("$.[*].attendanceDate").value(hasItem(DEFAULT_ATTENDANCE_DATE.toString())))
            .andExpect(jsonPath("$.[*].checkInTime").value(hasItem(DEFAULT_CHECK_IN_TIME.toString())))
            .andExpect(jsonPath("$.[*].checkOutTime").value(hasItem(DEFAULT_CHECK_OUT_TIME.toString())))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEmployeeAttendancesWithEagerRelationshipsIsEnabled() throws Exception {
        when(employeeAttendanceRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEmployeeAttendanceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(employeeAttendanceRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEmployeeAttendancesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(employeeAttendanceRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEmployeeAttendanceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(employeeAttendanceRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getEmployeeAttendance() throws Exception {
        // Initialize the database
        employeeAttendanceRepository.saveAndFlush(employeeAttendance);

        // Get the employeeAttendance
        restEmployeeAttendanceMockMvc
            .perform(get(ENTITY_API_URL_ID, employeeAttendance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employeeAttendance.getId().intValue()))
            .andExpect(jsonPath("$.attendanceDate").value(DEFAULT_ATTENDANCE_DATE.toString()))
            .andExpect(jsonPath("$.checkInTime").value(DEFAULT_CHECK_IN_TIME.toString()))
            .andExpect(jsonPath("$.checkOutTime").value(DEFAULT_CHECK_OUT_TIME.toString()))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS));
    }

    @Test
    @Transactional
    void getNonExistingEmployeeAttendance() throws Exception {
        // Get the employeeAttendance
        restEmployeeAttendanceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmployeeAttendance() throws Exception {
        // Initialize the database
        employeeAttendanceRepository.saveAndFlush(employeeAttendance);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employeeAttendance
        EmployeeAttendance updatedEmployeeAttendance = employeeAttendanceRepository.findById(employeeAttendance.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEmployeeAttendance are not directly saved in db
        em.detach(updatedEmployeeAttendance);
        updatedEmployeeAttendance
            .attendanceDate(UPDATED_ATTENDANCE_DATE)
            .checkInTime(UPDATED_CHECK_IN_TIME)
            .checkOutTime(UPDATED_CHECK_OUT_TIME)
            .comments(UPDATED_COMMENTS);

        restEmployeeAttendanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEmployeeAttendance.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedEmployeeAttendance))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeAttendance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEmployeeAttendanceToMatchAllProperties(updatedEmployeeAttendance);
    }

    @Test
    @Transactional
    void putNonExistingEmployeeAttendance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeAttendance.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeAttendanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeeAttendance.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(employeeAttendance))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeAttendance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmployeeAttendance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeAttendance.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeAttendanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(employeeAttendance))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeAttendance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmployeeAttendance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeAttendance.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeAttendanceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeAttendance)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeeAttendance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmployeeAttendanceWithPatch() throws Exception {
        // Initialize the database
        employeeAttendanceRepository.saveAndFlush(employeeAttendance);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employeeAttendance using partial update
        EmployeeAttendance partialUpdatedEmployeeAttendance = new EmployeeAttendance();
        partialUpdatedEmployeeAttendance.setId(employeeAttendance.getId());

        partialUpdatedEmployeeAttendance.checkInTime(UPDATED_CHECK_IN_TIME).checkOutTime(UPDATED_CHECK_OUT_TIME).comments(UPDATED_COMMENTS);

        restEmployeeAttendanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeeAttendance.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmployeeAttendance))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeAttendance in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmployeeAttendanceUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEmployeeAttendance, employeeAttendance),
            getPersistedEmployeeAttendance(employeeAttendance)
        );
    }

    @Test
    @Transactional
    void fullUpdateEmployeeAttendanceWithPatch() throws Exception {
        // Initialize the database
        employeeAttendanceRepository.saveAndFlush(employeeAttendance);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employeeAttendance using partial update
        EmployeeAttendance partialUpdatedEmployeeAttendance = new EmployeeAttendance();
        partialUpdatedEmployeeAttendance.setId(employeeAttendance.getId());

        partialUpdatedEmployeeAttendance
            .attendanceDate(UPDATED_ATTENDANCE_DATE)
            .checkInTime(UPDATED_CHECK_IN_TIME)
            .checkOutTime(UPDATED_CHECK_OUT_TIME)
            .comments(UPDATED_COMMENTS);

        restEmployeeAttendanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeeAttendance.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmployeeAttendance))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeAttendance in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmployeeAttendanceUpdatableFieldsEquals(
            partialUpdatedEmployeeAttendance,
            getPersistedEmployeeAttendance(partialUpdatedEmployeeAttendance)
        );
    }

    @Test
    @Transactional
    void patchNonExistingEmployeeAttendance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeAttendance.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeAttendanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, employeeAttendance.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(employeeAttendance))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeAttendance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmployeeAttendance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeAttendance.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeAttendanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(employeeAttendance))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeAttendance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmployeeAttendance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeAttendance.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeAttendanceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(employeeAttendance)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeeAttendance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmployeeAttendance() throws Exception {
        // Initialize the database
        employeeAttendanceRepository.saveAndFlush(employeeAttendance);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the employeeAttendance
        restEmployeeAttendanceMockMvc
            .perform(delete(ENTITY_API_URL_ID, employeeAttendance.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return employeeAttendanceRepository.count();
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

    protected EmployeeAttendance getPersistedEmployeeAttendance(EmployeeAttendance employeeAttendance) {
        return employeeAttendanceRepository.findById(employeeAttendance.getId()).orElseThrow();
    }

    protected void assertPersistedEmployeeAttendanceToMatchAllProperties(EmployeeAttendance expectedEmployeeAttendance) {
        assertEmployeeAttendanceAllPropertiesEquals(expectedEmployeeAttendance, getPersistedEmployeeAttendance(expectedEmployeeAttendance));
    }

    protected void assertPersistedEmployeeAttendanceToMatchUpdatableProperties(EmployeeAttendance expectedEmployeeAttendance) {
        assertEmployeeAttendanceAllUpdatablePropertiesEquals(
            expectedEmployeeAttendance,
            getPersistedEmployeeAttendance(expectedEmployeeAttendance)
        );
    }
}
