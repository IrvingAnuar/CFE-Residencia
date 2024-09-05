package com.cfe.cfetime.repository;

import com.cfe.cfetime.domain.StatusType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the StatusType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StatusTypeRepository extends JpaRepository<StatusType, Long> {}
