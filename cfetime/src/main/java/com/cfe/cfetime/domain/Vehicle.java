package com.cfe.cfetime.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Vehicle.
 */
@Entity
@Table(name = "vehicle")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Vehicle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * El nombre del vehiculo.
     */
    @Schema(description = "El nombre del vehiculo.", required = true)
    @NotNull
    @Size(max = 255)
    @Column(name = "descripcion", length = 255, nullable = false, unique = true)
    private String descripcion;

    /**
     * El modelo del vehiculo
     */
    @Schema(description = "El modelo del vehiculo", required = true)
    @NotNull
    @Size(max = 255)
    @Column(name = "model", length = 255, nullable = false, unique = true)
    private String model;

    /**
     * La placas del vehiculo
     */
    @Schema(description = "La placas del vehiculo", required = true)
    @NotNull
    @Size(max = 255)
    @Column(name = "plates", length = 255, nullable = false, unique = true)
    private String plates;

    /**
     * Vehiculo le pertenece un tipo de vehiculo.
     */
    @ManyToOne(optional = false)
    @NotNull
    private VehicleType vehicleType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Vehicle id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Vehicle descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getModel() {
        return this.model;
    }

    public Vehicle model(String model) {
        this.setModel(model);
        return this;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPlates() {
        return this.plates;
    }

    public Vehicle plates(String plates) {
        this.setPlates(plates);
        return this;
    }

    public void setPlates(String plates) {
        this.plates = plates;
    }

    public VehicleType getVehicleType() {
        return this.vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Vehicle vehicleType(VehicleType vehicleType) {
        this.setVehicleType(vehicleType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vehicle)) {
            return false;
        }
        return getId() != null && getId().equals(((Vehicle) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vehicle{" +
            "id=" + getId() +
            ", descripcion='" + getDescripcion() + "'" +
            ", model='" + getModel() + "'" +
            ", plates='" + getPlates() + "'" +
            "}";
    }
}
