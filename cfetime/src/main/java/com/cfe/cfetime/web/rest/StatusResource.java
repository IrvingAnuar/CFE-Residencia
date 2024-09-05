package com.cfe.cfetime.web.rest;

import com.cfe.cfetime.domain.Status;
import com.cfe.cfetime.repository.StatusRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.cfe.cfetime.domain.Status}.
 */
@RestController
@RequestMapping("/api/statuses")
@Transactional
public class StatusResource {

    private final Logger log = LoggerFactory.getLogger(StatusResource.class);

    private final StatusRepository statusRepository;

    public StatusResource(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    /**
     * {@code GET  /statuses} : get all the statuses.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of statuses in body.
     */
    @GetMapping("")
    public List<Status> getAllStatuses(@RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload) {
        log.debug("REST request to get all Statuses");
        if (eagerload) {
            return statusRepository.findAllWithEagerRelationships();
        } else {
            return statusRepository.findAll();
        }
    }

    /**
     * {@code GET  /statuses/:id} : get the "id" status.
     *
     * @param id the id of the status to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the status, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Status> getStatus(@PathVariable("id") Long id) {
        log.debug("REST request to get Status : {}", id);
        Optional<Status> status = statusRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(status);
    }
}
