package com.cfe.cfetime.repository;

import com.cfe.cfetime.domain.EmployeeAttendance;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EmployeeAttendance entity.
 */
@Repository
public interface EmployeeAttendanceRepository extends JpaRepository<EmployeeAttendance, Long> {
    default Optional<EmployeeAttendance> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<EmployeeAttendance> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<EmployeeAttendance> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select employeeAttendance from EmployeeAttendance employeeAttendance left join fetch employeeAttendance.employee left join fetch employeeAttendance.status",
        countQuery = "select count(employeeAttendance) from EmployeeAttendance employeeAttendance"
    )
    Page<EmployeeAttendance> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select employeeAttendance from EmployeeAttendance employeeAttendance left join fetch employeeAttendance.employee left join fetch employeeAttendance.status"
    )
    List<EmployeeAttendance> findAllWithToOneRelationships();

    @Query(
        "select employeeAttendance from EmployeeAttendance employeeAttendance left join fetch employeeAttendance.employee left join fetch employeeAttendance.status where employeeAttendance.id =:id"
    )
    Optional<EmployeeAttendance> findOneWithToOneRelationships(@Param("id") Long id);
}
