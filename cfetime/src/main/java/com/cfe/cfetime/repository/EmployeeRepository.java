package com.cfe.cfetime.repository;

import com.cfe.cfetime.domain.Employee;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Employee entity.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query("select employee from Employee employee where employee.created.login = ?#{authentication.name}")
    List<Employee> findByCreatedIsCurrentUser();

    @Query("select employee from Employee employee where employee.lastModified.login = ?#{authentication.name}")
    List<Employee> findByLastModifiedIsCurrentUser();

    default Optional<Employee> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Employee> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Employee> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select employee from Employee employee left join fetch employee.user left join fetch employee.status left join fetch employee.created left join fetch employee.lastModified",
        countQuery = "select count(employee) from Employee employee"
    )
    Page<Employee> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select employee from Employee employee left join fetch employee.user left join fetch employee.status left join fetch employee.created left join fetch employee.lastModified"
    )
    List<Employee> findAllWithToOneRelationships();

    @Query(
        "select employee from Employee employee left join fetch employee.user left join fetch employee.status left join fetch employee.created left join fetch employee.lastModified where employee.id =:id"
    )
    Optional<Employee> findOneWithToOneRelationships(@Param("id") Long id);
}
