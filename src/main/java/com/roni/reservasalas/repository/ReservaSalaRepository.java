package com.roni.reservasalas.repository;

import com.roni.reservasalas.domain.ReservaSala;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the ReservaSala entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReservaSalaRepository extends JpaRepository<ReservaSala, Long> {

    @Query("select reserva_sala from ReservaSala reserva_sala where reserva_sala.user.login = ?#{principal.username}")
    List<ReservaSala> findByUserIsCurrentUser();

}
