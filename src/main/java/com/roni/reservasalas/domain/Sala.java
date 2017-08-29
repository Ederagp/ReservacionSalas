package com.roni.reservasalas.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Sala.
 */
@Entity
@Table(name = "sala")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Sala implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "ubicacion")
    private String ubicacion;

    @Column(name = "capacidad")
    private String capacidad;

    @Column(name = "equipamiento")
    private String equipamiento;

    @Column(name = "observaciones")
    private String observaciones;

    @OneToMany(mappedBy = "sala")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ReservaSala> reservaSalas = new HashSet<>();

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public Sala nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public Sala ubicacion(String ubicacion) {
        this.ubicacion = ubicacion;
        return this;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getCapacidad() {
        return capacidad;
    }

    public Sala capacidad(String capacidad) {
        this.capacidad = capacidad;
        return this;
    }

    public void setCapacidad(String capacidad) {
        this.capacidad = capacidad;
    }

    public String getEquipamiento() {
        return equipamiento;
    }

    public Sala equipamiento(String equipamiento) {
        this.equipamiento = equipamiento;
        return this;
    }

    public void setEquipamiento(String equipamiento) {
        this.equipamiento = equipamiento;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public Sala observaciones(String observaciones) {
        this.observaciones = observaciones;
        return this;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Set<ReservaSala> getReservaSalas() {
        return reservaSalas;
    }

    public Sala reservaSalas(Set<ReservaSala> reservaSalas) {
        this.reservaSalas = reservaSalas;
        return this;
    }

    public Sala addReservaSala(ReservaSala reservaSala) {
        this.reservaSalas.add(reservaSala);
        reservaSala.setSala(this);
        return this;
    }

    public Sala removeReservaSala(ReservaSala reservaSala) {
        this.reservaSalas.remove(reservaSala);
        reservaSala.setSala(null);
        return this;
    }

    public void setReservaSalas(Set<ReservaSala> reservaSalas) {
        this.reservaSalas = reservaSalas;
    }
    // jhipster-needle-entity-add-getters-setters - Jhipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Sala sala = (Sala) o;
        if (sala.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sala.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Sala{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", ubicacion='" + getUbicacion() + "'" +
            ", capacidad='" + getCapacidad() + "'" +
            ", equipamiento='" + getEquipamiento() + "'" +
            ", observaciones='" + getObservaciones() + "'" +
            "}";
    }
}
