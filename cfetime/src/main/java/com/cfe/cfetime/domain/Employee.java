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
 * Tabla que almacena información de los empleados.
 */
@Schema(description = "Tabla que almacena información de los empleados.")
@Entity
@Table(name = "employee")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "employeeIdSeq")
    @Column(name = "id")
    private Long id;

    /**
     * Clave del empleado.
     */
    @Schema(description = "Clave del empleado.")
    @Column(name = "clave")
    private Integer clave;

    /**
     * Nombre del empleado.
     */
    @Schema(description = "Nombre del empleado.")
    @Size(max = 100)
    @Column(name = "name", length = 100)
    private String name;

    /**
     * Primer Apellido del empleado.
     */
    @Schema(description = "Primer Apellido del empleado.")
    @Size(max = 100)
    @Column(name = "first_surname", length = 100)
    private String firstSurname;

    /**
     * Segundo Apellido del empleado.
     */
    @Schema(description = "Segundo Apellido del empleado.")
    @Size(max = 100)
    @Column(name = "second_surname", length = 100)
    private String secondSurname;

    /**
     * La fecha en que se creó.
     */
    @Schema(description = "La fecha en que se creó.", required = true)
    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    /**
     * La fecha en que se modificó por última vez.
     */
    @Schema(description = "La fecha en que se modificó por última vez.")
    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    /**
     * Puede estar asociado a un usuario.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private User user;

    /**
     * Le pertenece un estatus.
     */
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "statusType" }, allowSetters = true)
    private Status status;

    /**
     * Está asociado con un usuario para la creación.
     */
    @ManyToOne(optional = false)
    @NotNull
    private User created;

    /**
     * Asociado con un usuario para última modificación.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private User lastModified;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Employee id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getClave() {
        return this.clave;
    }

    public Employee clave(Integer clave) {
        this.setClave(clave);
        return this;
    }

    public void setClave(Integer clave) {
        this.clave = clave;
    }

    public String getName() {
        return this.name;
    }

    public Employee name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstSurname() {
        return this.firstSurname;
    }

    public Employee firstSurname(String firstSurname) {
        this.setFirstSurname(firstSurname);
        return this;
    }

    public void setFirstSurname(String firstSurname) {
        this.firstSurname = firstSurname;
    }

    public String getSecondSurname() {
        return this.secondSurname;
    }

    public Employee secondSurname(String secondSurname) {
        this.setSecondSurname(secondSurname);
        return this;
    }

    public void setSecondSurname(String secondSurname) {
        this.secondSurname = secondSurname;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public Employee createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public Employee lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Employee user(User user) {
        this.setUser(user);
        return this;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Employee status(Status status) {
        this.setStatus(status);
        return this;
    }

    public User getCreated() {
        return this.created;
    }

    public void setCreated(User user) {
        this.created = user;
    }

    public Employee created(User user) {
        this.setCreated(user);
        return this;
    }

    public User getLastModified() {
        return this.lastModified;
    }

    public void setLastModified(User user) {
        this.lastModified = user;
    }

    public Employee lastModified(User user) {
        this.setLastModified(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Employee)) {
            return false;
        }
        return getId() != null && getId().equals(((Employee) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Employee{" +
            "id=" + getId() +
            ", clave=" + getClave() +
            ", name='" + getName() + "'" +
            ", firstSurname='" + getFirstSurname() + "'" +
            ", secondSurname='" + getSecondSurname() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
