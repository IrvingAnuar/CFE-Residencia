package com.cfe.cfetime.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Tabla que almacena la información de uso de vehículos.
 */
@Schema(description = "Tabla que almacena la información de uso de vehículos.")
@Entity
@Table(name = "vehicle_usage")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VehicleUsage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "vehicleUsageIdSeq")
    @Column(name = "id")
    private Long id;

    /**
     * Fecha de solicitud del vehículo.
     */
    @Schema(description = "Fecha de solicitud del vehículo.", required = true)
    @NotNull
    @Column(name = "start_date", nullable = false)
    private Instant startDate;

    /**
     * Fecha de devolución del vehículo (puede ser nulo si aún no se ha devuelto).
     */
    @Schema(description = "Fecha de devolución del vehículo (puede ser nulo si aún no se ha devuelto).")
    @Column(name = "end_date")
    private Instant endDate;

    /**
     * Descripción o comentarios adicionales sobre el uso del vehículo.
     */
    @Schema(description = "Descripción o comentarios adicionales sobre el uso del vehículo.")
    @Column(name = "comments")
    private String comments;

    /**
     * Un registro de uso le pertenece a un vehículo.
     */
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "vehicleType" }, allowSetters = true)
    private Vehicle vehicle;

    /**
     * Un registro de uso le pertenece a un empleado.
     */
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user", "status", "created", "lastModified" }, allowSetters = true)
    private Employee employee;

    /**
     * Un registro de uso tiene un estatus.
     */
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "statusType" }, allowSetters = true)
    private Status status;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public VehicleUsage id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public VehicleUsage startDate(Instant startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return this.endDate;
    }

    public VehicleUsage endDate(Instant endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public String getComments() {
        return this.comments;
    }

    public VehicleUsage comments(String comments) {
        this.setComments(comments);
        return this;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Vehicle getVehicle() {
        return this.vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public VehicleUsage vehicle(Vehicle vehicle) {
        this.setVehicle(vehicle);
        return this;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public VehicleUsage employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public VehicleUsage status(Status status) {
        this.setStatus(status);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VehicleUsage)) {
            return false;
        }
        return getId() != null && getId().equals(((VehicleUsage) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VehicleUsage{" +
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", comments='" + getComments() + "'" +
            "}";
    }
}
