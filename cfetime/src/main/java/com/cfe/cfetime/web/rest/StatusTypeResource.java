package com.cfe.cfetime.web.rest;

import com.cfe.cfetime.domain.StatusType;
import com.cfe.cfetime.repository.StatusTypeRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.cfe.cfetime.domain.StatusType}.
 */
@RestController
@RequestMapping("/api/status-types")
@Transactional
public class StatusTypeResource {

    private final Logger log = LoggerFactory.getLogger(StatusTypeResource.class);

    private final StatusTypeRepository statusTypeRepository;

    public StatusTypeResource(StatusTypeRepository statusTypeRepository) {
        this.statusTypeRepository = statusTypeRepository;
    }

    /**
     * {@code GET  /status-types} : get all the statusTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of statusTypes in body.
     */
    @GetMapping("")
    public List<StatusType> getAllStatusTypes() {
        log.debug("REST request to get all StatusTypes");
        return statusTypeRepository.findAll();
    }

    /**
     * {@code GET  /status-types/:id} : get the "id" statusType.
     *
     * @param id the id of the statusType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the statusType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StatusType> getStatusType(@PathVariable("id") Long id) {
        log.debug("REST request to get StatusType : {}", id);
        Optional<StatusType> statusType = statusTypeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(statusType);
    }
}
