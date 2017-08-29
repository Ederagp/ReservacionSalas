package com.roni.reservasalas.repository;

import com.roni.reservasalas.domain.Sala;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Sala entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SalaRepository extends JpaRepository<Sala, Long> {

}
