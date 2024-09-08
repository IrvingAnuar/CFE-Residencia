package com.cfe.cfetime.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Tabla que almacena la información de asistencia de empleados.
 */
@Schema(description = "Tabla que almacena la información de asistencia de empleados.")
@Entity
@Table(name = "employee_attendance")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmployeeAttendance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "employeeAttendanceIdSeq")
    @Column(name = "id")
    private Long id;

    /**
     * Fecha de la asistencia.
     */
    @Schema(description = "Fecha de la asistencia.", required = true)
    @NotNull
    @Column(name = "attendance_date", nullable = false)
    private LocalDate attendanceDate;

    /**
     * Hora de entrada del empleado.
     */
    @Schema(description = "Hora de entrada del empleado.", required = true)
    @NotNull
    @Column(name = "check_in_time", nullable = false)
    private Instant checkInTime;

    /**
     * Hora de salida del empleado.
     */
    @Schema(description = "Hora de salida del empleado.")
    @Column(name = "check_out_time")
    private Instant checkOutTime;

    /**
     * Comentarios adicionales o notas.
     */
    @Schema(description = "Comentarios adicionales o notas.")
    @Column(name = "comments")
    private String comments;

    /**
     * La asistencia pertenece a un empleado.
     */
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user", "status", "created", "lastModified" }, allowSetters = true)
    private Employee employee;

    /**
     * La asistencia puede tener un estatus (presente, ausente, etc.).
     */
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "statusType" }, allowSetters = true)
    private Status status;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EmployeeAttendance id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getAttendanceDate() {
        return this.attendanceDate;
    }

    public EmployeeAttendance attendanceDate(LocalDate attendanceDate) {
        this.setAttendanceDate(attendanceDate);
        return this;
    }

    public void setAttendanceDate(LocalDate attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public Instant getCheckInTime() {
        return this.checkInTime;
    }

    public EmployeeAttendance checkInTime(Instant checkInTime) {
        this.setCheckInTime(checkInTime);
        return this;
    }

    public void setCheckInTime(Instant checkInTime) {
        this.checkInTime = checkInTime;
    }

    public Instant getCheckOutTime() {
        return this.checkOutTime;
    }

    public EmployeeAttendance checkOutTime(Instant checkOutTime) {
        this.setCheckOutTime(checkOutTime);
        return this;
    }

    public void setCheckOutTime(Instant checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public String getComments() {
        return this.comments;
    }

    public EmployeeAttendance comments(String comments) {
        this.setComments(comments);
        return this;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public EmployeeAttendance employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public EmployeeAttendance status(Status status) {
        this.setStatus(status);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmployeeAttendance)) {
            return false;
        }
        return getId() != null && getId().equals(((EmployeeAttendance) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployeeAttendance{" +
            "id=" + getId() +
            ", attendanceDate='" + getAttendanceDate() + "'" +
            ", checkInTime='" + getCheckInTime() + "'" +
            ", checkOutTime='" + getCheckOutTime() + "'" +
            ", comments='" + getComments() + "'" +
            "}";
    }
}
