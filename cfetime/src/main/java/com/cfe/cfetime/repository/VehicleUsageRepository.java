package com.cfe.cfetime.repository;

import com.cfe.cfetime.domain.VehicleUsage;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the VehicleUsage entity.
 */
@Repository
public interface VehicleUsageRepository extends JpaRepository<VehicleUsage, Long> {
    default Optional<VehicleUsage> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<VehicleUsage> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<VehicleUsage> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select vehicleUsage from VehicleUsage vehicleUsage left join fetch vehicleUsage.vehicle left join fetch vehicleUsage.employee left join fetch vehicleUsage.status",
        countQuery = "select count(vehicleUsage) from VehicleUsage vehicleUsage"
    )
    Page<VehicleUsage> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select vehicleUsage from VehicleUsage vehicleUsage left join fetch vehicleUsage.vehicle left join fetch vehicleUsage.employee left join fetch vehicleUsage.status"
    )
    List<VehicleUsage> findAllWithToOneRelationships();

    @Query(
        "select vehicleUsage from VehicleUsage vehicleUsage left join fetch vehicleUsage.vehicle left join fetch vehicleUsage.employee left join fetch vehicleUsage.status where vehicleUsage.id =:id"
    )
    Optional<VehicleUsage> findOneWithToOneRelationships(@Param("id") Long id);
}
