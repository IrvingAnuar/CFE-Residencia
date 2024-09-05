package com.cfe.cfetime.web.rest;

import com.cfe.cfetime.domain.EmployeeAttendance;
import com.cfe.cfetime.repository.EmployeeAttendanceRepository;
import com.cfe.cfetime.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.cfe.cfetime.domain.EmployeeAttendance}.
 */
@RestController
@RequestMapping("/api/employee-attendances")
@Transactional
public class EmployeeAttendanceResource {

    private final Logger log = LoggerFactory.getLogger(EmployeeAttendanceResource.class);

    private static final String ENTITY_NAME = "employeeAttendance";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmployeeAttendanceRepository employeeAttendanceRepository;

    public EmployeeAttendanceResource(EmployeeAttendanceRepository employeeAttendanceRepository) {
        this.employeeAttendanceRepository = employeeAttendanceRepository;
    }

    /**
     * {@code POST  /employee-attendances} : Create a new employeeAttendance.
     *
     * @param employeeAttendance the employeeAttendance to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new employeeAttendance, or with status {@code 400 (Bad Request)} if the employeeAttendance has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EmployeeAttendance> createEmployeeAttendance(@Valid @RequestBody EmployeeAttendance employeeAttendance)
        throws URISyntaxException {
        log.debug("REST request to save EmployeeAttendance : {}", employeeAttendance);
        if (employeeAttendance.getId() != null) {
            throw new BadRequestAlertException("A new employeeAttendance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        employeeAttendance = employeeAttendanceRepository.save(employeeAttendance);
        return ResponseEntity.created(new URI("/api/employee-attendances/" + employeeAttendance.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, employeeAttendance.getId().toString()))
            .body(employeeAttendance);
    }

    /**
     * {@code PUT  /employee-attendances/:id} : Updates an existing employeeAttendance.
     *
     * @param id the id of the employeeAttendance to save.
     * @param employeeAttendance the employeeAttendance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeeAttendance,
     * or with status {@code 400 (Bad Request)} if the employeeAttendance is not valid,
     * or with status {@code 500 (Internal Server Error)} if the employeeAttendance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeAttendance> updateEmployeeAttendance(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EmployeeAttendance employeeAttendance
    ) throws URISyntaxException {
        log.debug("REST request to update EmployeeAttendance : {}, {}", id, employeeAttendance);
        if (employeeAttendance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employeeAttendance.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employeeAttendanceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        employeeAttendance = employeeAttendanceRepository.save(employeeAttendance);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employeeAttendance.getId().toString()))
            .body(employeeAttendance);
    }

    /**
     * {@code PATCH  /employee-attendances/:id} : Partial updates given fields of an existing employeeAttendance, field will ignore if it is null
     *
     * @param id the id of the employeeAttendance to save.
     * @param employeeAttendance the employeeAttendance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeeAttendance,
     * or with status {@code 400 (Bad Request)} if the employeeAttendance is not valid,
     * or with status {@code 404 (Not Found)} if the employeeAttendance is not found,
     * or with status {@code 500 (Internal Server Error)} if the employeeAttendance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EmployeeAttendance> partialUpdateEmployeeAttendance(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EmployeeAttendance employeeAttendance
    ) throws URISyntaxException {
        log.debug("REST request to partial update EmployeeAttendance partially : {}, {}", id, employeeAttendance);
        if (employeeAttendance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employeeAttendance.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employeeAttendanceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EmployeeAttendance> result = employeeAttendanceRepository
            .findById(employeeAttendance.getId())
            .map(existingEmployeeAttendance -> {
                if (employeeAttendance.getAttendanceDate() != null) {
                    existingEmployeeAttendance.setAttendanceDate(employeeAttendance.getAttendanceDate());
                }
                if (employeeAttendance.getCheckInTime() != null) {
                    existingEmployeeAttendance.setCheckInTime(employeeAttendance.getCheckInTime());
                }
                if (employeeAttendance.getCheckOutTime() != null) {
                    existingEmployeeAttendance.setCheckOutTime(employeeAttendance.getCheckOutTime());
                }
                if (employeeAttendance.getComments() != null) {
                    existingEmployeeAttendance.setComments(employeeAttendance.getComments());
                }

                return existingEmployeeAttendance;
            })
            .map(employeeAttendanceRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employeeAttendance.getId().toString())
        );
    }

    /**
     * {@code GET  /employee-attendances} : get all the employeeAttendances.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of employeeAttendances in body.
     */
    @GetMapping("")
    public List<EmployeeAttendance> getAllEmployeeAttendances(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get all EmployeeAttendances");
        if (eagerload) {
            return employeeAttendanceRepository.findAllWithEagerRelationships();
        } else {
            return employeeAttendanceRepository.findAll();
        }
    }

    /**
     * {@code GET  /employee-attendances/:id} : get the "id" employeeAttendance.
     *
     * @param id the id of the employeeAttendance to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the employeeAttendance, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeAttendance> getEmployeeAttendance(@PathVariable("id") Long id) {
        log.debug("REST request to get EmployeeAttendance : {}", id);
        Optional<EmployeeAttendance> employeeAttendance = employeeAttendanceRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(employeeAttendance);
    }

    /**
     * {@code DELETE  /employee-attendances/:id} : delete the "id" employeeAttendance.
     *
     * @param id the id of the employeeAttendance to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployeeAttendance(@PathVariable("id") Long id) {
        log.debug("REST request to delete EmployeeAttendance : {}", id);
        employeeAttendanceRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
