package com.roni.reservasalas.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.roni.reservasalas.domain.ReservaSala;

import com.roni.reservasalas.repository.ReservaSalaRepository;
import com.roni.reservasalas.service.MailService;
import com.roni.reservasalas.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ReservaSala.
 */
@RestController
@RequestMapping("/api")
public class ReservaSalaResource {

    private final Logger log = LoggerFactory.getLogger(ReservaSalaResource.class);

    private static final String ENTITY_NAME = "reservaSala";

    private final ReservaSalaRepository reservaSalaRepository;

    private final MailService mailService;

    public ReservaSalaResource(ReservaSalaRepository reservaSalaRepository, MailService mailService) {
        this.reservaSalaRepository = reservaSalaRepository;
        this.mailService = mailService;
    }

    /**
     * POST  /reserva-salas : Create a new reservaSala.
     *
     * @param reservaSala the reservaSala to create
     * @return the ResponseEntity with status 201 (Created) and with body the new reservaSala, or with status 400 (Bad Request) if the reservaSala has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/reserva-salas")
    @Timed
    public ResponseEntity<ReservaSala> createReservaSala(@RequestBody ReservaSala reservaSala) throws URISyntaxException {
        log.debug("REST request to save ReservaSala : {}", reservaSala);
        if (reservaSala.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new reservaSala cannot already have an ID")).body(null);
        }
        ReservaSala result = reservaSalaRepository.save(reservaSala);
        mailService.sendReservacionMail(result);
        return ResponseEntity.created(new URI("/api/reserva-salas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /reserva-salas : Updates an existing reservaSala.
     *
     * @param reservaSala the reservaSala to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated reservaSala,
     * or with status 400 (Bad Request) if the reservaSala is not valid,
     * or with status 500 (Internal Server Error) if the reservaSala couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/reserva-salas")
    @Timed
    public ResponseEntity<ReservaSala> updateReservaSala(@RequestBody ReservaSala reservaSala) throws URISyntaxException {
        log.debug("REST request to update ReservaSala : {}", reservaSala);
        if (reservaSala.getId() == null) {
            return createReservaSala(reservaSala);
        }
        ReservaSala result = reservaSalaRepository.save(reservaSala);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, reservaSala.getId().toString()))
            .body(result);
    }

    /**
     * GET  /reserva-salas : get all the reservaSalas.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of reservaSalas in body
     */
    @GetMapping("/reserva-salas")
    @Timed
    public List<ReservaSala> getAllReservaSalas() {
        log.debug("REST request to get all ReservaSalas");
        return reservaSalaRepository.findAll();
        }

    /**
     * GET  /reserva-salas : get all the reservaSalasBetweenFechaInicial.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of reservaSalas in body
     */
    @GetMapping("/reserva-salas/sala/{id}")
    @Timed
    public List<ReservaSala> getAllReservaSalasBySala(@PathVariable Long id) {
        log.debug("REST request to get all ReservaSalasBySala : {}", id);
        List<ReservaSala> reservaSala = reservaSalaRepository.findAllBySalaId(id);

        return reservaSala;
    }

    /**
     * GET  /reserva-salas/:id : get the "id" reservaSala.
     *
     * @param id the id of the reservaSala to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the reservaSala, or with status 404 (Not Found)
     */
    @GetMapping("/reserva-salas/{id}")
    @Timed
    public ResponseEntity<ReservaSala> getReservaSala(@PathVariable Long id) {
        log.debug("REST request to get ReservaSala : {}", id);
        ReservaSala reservaSala = reservaSalaRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(reservaSala));
    }

    /**
     * DELETE  /reserva-salas/:id : delete the "id" reservaSala.
     *
     * @param id the id of the reservaSala to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/reserva-salas/{id}")
    @Timed
    public ResponseEntity<Void> deleteReservaSala(@PathVariable Long id) {
        log.debug("REST request to delete ReservaSala : {}", id);
        reservaSalaRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
