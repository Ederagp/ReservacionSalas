package com.roni.reservasalas.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import com.roni.reservasalas.domain.enumeration.Estado;

/**
 * A ReservaSala.
 */
@Entity
@Table(name = "reserva_sala")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ReservaSala implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "fecha_hora_inicial")
    private LocalDate fechaHoraInicial;

    @Column(name = "fecha_hora_final")
    private LocalDate fechaHoraFinal;

    @Column(name = "descripcion")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private Estado estado;

    @ManyToOne
    private Sala sala;

    @ManyToOne
    private User user;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public ReservaSala titulo(String titulo) {
        this.titulo = titulo;
        return this;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public LocalDate getFechaHoraInicial() {
        return fechaHoraInicial;
    }

    public ReservaSala fechaHoraInicial(LocalDate fechaHoraInicial) {
        this.fechaHoraInicial = fechaHoraInicial;
        return this;
    }

    public void setFechaHoraInicial(LocalDate fechaHoraInicial) {
        this.fechaHoraInicial = fechaHoraInicial;
    }

    public LocalDate getFechaHoraFinal() {
        return fechaHoraFinal;
    }

    public ReservaSala fechaHoraFinal(LocalDate fechaHoraFinal) {
        this.fechaHoraFinal = fechaHoraFinal;
        return this;
    }

    public void setFechaHoraFinal(LocalDate fechaHoraFinal) {
        this.fechaHoraFinal = fechaHoraFinal;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public ReservaSala descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Estado getEstado() {
        return estado;
    }

    public ReservaSala estado(Estado estado) {
        this.estado = estado;
        return this;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Sala getSala() {
        return sala;
    }

    public ReservaSala sala(Sala sala) {
        this.sala = sala;
        return this;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    public User getUser() {
        return user;
    }

    public ReservaSala user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
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
        ReservaSala reservaSala = (ReservaSala) o;
        if (reservaSala.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), reservaSala.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ReservaSala{" +
            "id=" + getId() +
            ", titulo='" + getTitulo() + "'" +
            ", fechaHoraInicial='" + getFechaHoraInicial() + "'" +
            ", fechaHoraFinal='" + getFechaHoraFinal() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", estado='" + getEstado() + "'" +
            "}";
    }
}
