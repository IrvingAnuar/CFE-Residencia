package com.cfe.cfetime.web.rest;

import com.cfe.cfetime.domain.EmployeeType;
import com.cfe.cfetime.repository.EmployeeTypeRepository;
import com.cfe.cfetime.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.cfe.cfetime.domain.EmployeeType}.
 */
@RestController
@RequestMapping("/api/employee-types")
@Transactional
public class EmployeeTypeResource {

    private final Logger log = LoggerFactory.getLogger(EmployeeTypeResource.class);

    private static final String ENTITY_NAME = "employeeType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmployeeTypeRepository employeeTypeRepository;

    public EmployeeTypeResource(EmployeeTypeRepository employeeTypeRepository) {
        this.employeeTypeRepository = employeeTypeRepository;
    }

    /**
     * {@code POST  /employee-types} : Create a new employeeType.
     *
     * @param employeeType the employeeType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new employeeType, or with status {@code 400 (Bad Request)} if the employeeType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EmployeeType> createEmployeeType(@RequestBody EmployeeType employeeType) throws URISyntaxException {
        log.debug("REST request to save EmployeeType : {}", employeeType);
        if (employeeType.getId() != null) {
            throw new BadRequestAlertException("A new employeeType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        employeeType = employeeTypeRepository.save(employeeType);
        return ResponseEntity.created(new URI("/api/employee-types/" + employeeType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, employeeType.getId().toString()))
            .body(employeeType);
    }

    /**
     * {@code PUT  /employee-types/:id} : Updates an existing employeeType.
     *
     * @param id the id of the employeeType to save.
     * @param employeeType the employeeType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeeType,
     * or with status {@code 400 (Bad Request)} if the employeeType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the employeeType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeType> updateEmployeeType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EmployeeType employeeType
    ) throws URISyntaxException {
        log.debug("REST request to update EmployeeType : {}, {}", id, employeeType);
        if (employeeType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employeeType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employeeTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        employeeType = employeeTypeRepository.save(employeeType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employeeType.getId().toString()))
            .body(employeeType);
    }

    /**
     * {@code PATCH  /employee-types/:id} : Partial updates given fields of an existing employeeType, field will ignore if it is null
     *
     * @param id the id of the employeeType to save.
     * @param employeeType the employeeType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeeType,
     * or with status {@code 400 (Bad Request)} if the employeeType is not valid,
     * or with status {@code 404 (Not Found)} if the employeeType is not found,
     * or with status {@code 500 (Internal Server Error)} if the employeeType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EmployeeType> partialUpdateEmployeeType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EmployeeType employeeType
    ) throws URISyntaxException {
        log.debug("REST request to partial update EmployeeType partially : {}, {}", id, employeeType);
        if (employeeType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employeeType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employeeTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EmployeeType> result = employeeTypeRepository
            .findById(employeeType.getId())
            .map(existingEmployeeType -> {
                if (employeeType.getName() != null) {
                    existingEmployeeType.setName(employeeType.getName());
                }

                return existingEmployeeType;
            })
            .map(employeeTypeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employeeType.getId().toString())
        );
    }

    /**
     * {@code GET  /employee-types} : get all the employeeTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of employeeTypes in body.
     */
    @GetMapping("")
    public List<EmployeeType> getAllEmployeeTypes() {
        log.debug("REST request to get all EmployeeTypes");
        return employeeTypeRepository.findAll();
    }

    /**
     * {@code GET  /employee-types/:id} : get the "id" employeeType.
     *
     * @param id the id of the employeeType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the employeeType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeType> getEmployeeType(@PathVariable("id") Long id) {
        log.debug("REST request to get EmployeeType : {}", id);
        Optional<EmployeeType> employeeType = employeeTypeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(employeeType);
    }

    /**
     * {@code DELETE  /employee-types/:id} : delete the "id" employeeType.
     *
     * @param id the id of the employeeType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployeeType(@PathVariable("id") Long id) {
        log.debug("REST request to delete EmployeeType : {}", id);
        employeeTypeRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
