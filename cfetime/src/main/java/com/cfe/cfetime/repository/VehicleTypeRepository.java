package com.cfe.cfetime.repository;

import com.cfe.cfetime.domain.VehicleType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the VehicleType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VehicleTypeRepository extends JpaRepository<VehicleType, Long> {}
