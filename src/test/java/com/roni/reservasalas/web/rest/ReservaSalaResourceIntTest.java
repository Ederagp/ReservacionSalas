package com.roni.reservasalas.web.rest;

import com.roni.reservasalas.ReservaSalasApp;

import com.roni.reservasalas.domain.ReservaSala;
import com.roni.reservasalas.repository.ReservaSalaRepository;
import com.roni.reservasalas.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.roni.reservasalas.domain.enumeration.Estado;
/**
 * Test class for the ReservaSalaResource REST controller.
 *
 * @see ReservaSalaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReservaSalasApp.class)
public class ReservaSalaResourceIntTest {

    private static final String DEFAULT_TITULO = "AAAAAAAAAA";
    private static final String UPDATED_TITULO = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_FECHA_HORA_INICIAL = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FECHA_HORA_INICIAL = ZonedDateTime.now(ZoneId.systemDefault());

    private static final ZonedDateTime DEFAULT_FECHA_HORA_FINAL = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FECHA_HORA_FINAL = ZonedDateTime.now(ZoneId.systemDefault());

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final Estado DEFAULT_ESTADO = Estado.Reservada;
    private static final Estado UPDATED_ESTADO = Estado.Cancelada;

    @Autowired
    private ReservaSalaRepository reservaSalaRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restReservaSalaMockMvc;

    private ReservaSala reservaSala;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ReservaSalaResource reservaSalaResource = new ReservaSalaResource(reservaSalaRepository);
        this.restReservaSalaMockMvc = MockMvcBuilders.standaloneSetup(reservaSalaResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReservaSala createEntity(EntityManager em) {
        ReservaSala reservaSala = new ReservaSala()
            .titulo(DEFAULT_TITULO)
            .fechaHoraInicial(DEFAULT_FECHA_HORA_INICIAL)
            .fechaHoraFinal(DEFAULT_FECHA_HORA_FINAL)
            .descripcion(DEFAULT_DESCRIPCION)
            .estado(DEFAULT_ESTADO);
        return reservaSala;
    }

    @Before
    public void initTest() {
        reservaSala = createEntity(em);
    }

    @Test
    @Transactional
    public void createReservaSala() throws Exception {
        int databaseSizeBeforeCreate = reservaSalaRepository.findAll().size();

        // Create the ReservaSala
        restReservaSalaMockMvc.perform(post("/api/reserva-salas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reservaSala)))
            .andExpect(status().isCreated());

        // Validate the ReservaSala in the database
        List<ReservaSala> reservaSalaList = reservaSalaRepository.findAll();
        assertThat(reservaSalaList).hasSize(databaseSizeBeforeCreate + 1);
        ReservaSala testReservaSala = reservaSalaList.get(reservaSalaList.size() - 1);
        assertThat(testReservaSala.getTitulo()).isEqualTo(DEFAULT_TITULO);
        assertThat(testReservaSala.getFechaHoraInicial()).isEqualTo(DEFAULT_FECHA_HORA_INICIAL);
        assertThat(testReservaSala.getFechaHoraFinal()).isEqualTo(DEFAULT_FECHA_HORA_FINAL);
        assertThat(testReservaSala.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testReservaSala.getEstado()).isEqualTo(DEFAULT_ESTADO);
    }

    @Test
    @Transactional
    public void createReservaSalaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = reservaSalaRepository.findAll().size();

        // Create the ReservaSala with an existing ID
        reservaSala.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReservaSalaMockMvc.perform(post("/api/reserva-salas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reservaSala)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ReservaSala> reservaSalaList = reservaSalaRepository.findAll();
        assertThat(reservaSalaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllReservaSalas() throws Exception {
        // Initialize the database
        reservaSalaRepository.saveAndFlush(reservaSala);

        // Get all the reservaSalaList
        restReservaSalaMockMvc.perform(get("/api/reserva-salas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reservaSala.getId().intValue())))
            .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO.toString())))
            .andExpect(jsonPath("$.[*].fechaHoraInicial").value(hasItem(DEFAULT_FECHA_HORA_INICIAL.toString())))
            .andExpect(jsonPath("$.[*].fechaHoraFinal").value(hasItem(DEFAULT_FECHA_HORA_FINAL.toString())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())));
    }

    @Test
    @Transactional
    public void getReservaSala() throws Exception {
        // Initialize the database
        reservaSalaRepository.saveAndFlush(reservaSala);

        // Get the reservaSala
        restReservaSalaMockMvc.perform(get("/api/reserva-salas/{id}", reservaSala.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(reservaSala.getId().intValue()))
            .andExpect(jsonPath("$.titulo").value(DEFAULT_TITULO.toString()))
            .andExpect(jsonPath("$.fechaHoraInicial").value(DEFAULT_FECHA_HORA_INICIAL.toString()))
            .andExpect(jsonPath("$.fechaHoraFinal").value(DEFAULT_FECHA_HORA_FINAL.toString()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingReservaSala() throws Exception {
        // Get the reservaSala
        restReservaSalaMockMvc.perform(get("/api/reserva-salas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReservaSala() throws Exception {
        // Initialize the database
        reservaSalaRepository.saveAndFlush(reservaSala);
        int databaseSizeBeforeUpdate = reservaSalaRepository.findAll().size();

        // Update the reservaSala
        ReservaSala updatedReservaSala = reservaSalaRepository.findOne(reservaSala.getId());
        updatedReservaSala
            .titulo(UPDATED_TITULO)
            .fechaHoraInicial(UPDATED_FECHA_HORA_INICIAL)
            .fechaHoraFinal(UPDATED_FECHA_HORA_FINAL)
            .descripcion(UPDATED_DESCRIPCION)
            .estado(UPDATED_ESTADO);

        restReservaSalaMockMvc.perform(put("/api/reserva-salas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedReservaSala)))
            .andExpect(status().isOk());

        // Validate the ReservaSala in the database
        List<ReservaSala> reservaSalaList = reservaSalaRepository.findAll();
        assertThat(reservaSalaList).hasSize(databaseSizeBeforeUpdate);
        ReservaSala testReservaSala = reservaSalaList.get(reservaSalaList.size() - 1);
        assertThat(testReservaSala.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testReservaSala.getFechaHoraInicial()).isEqualTo(UPDATED_FECHA_HORA_INICIAL);
        assertThat(testReservaSala.getFechaHoraFinal()).isEqualTo(UPDATED_FECHA_HORA_FINAL);
        assertThat(testReservaSala.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testReservaSala.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    public void updateNonExistingReservaSala() throws Exception {
        int databaseSizeBeforeUpdate = reservaSalaRepository.findAll().size();

        // Create the ReservaSala

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restReservaSalaMockMvc.perform(put("/api/reserva-salas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reservaSala)))
            .andExpect(status().isCreated());

        // Validate the ReservaSala in the database
        List<ReservaSala> reservaSalaList = reservaSalaRepository.findAll();
        assertThat(reservaSalaList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteReservaSala() throws Exception {
        // Initialize the database
        reservaSalaRepository.saveAndFlush(reservaSala);
        int databaseSizeBeforeDelete = reservaSalaRepository.findAll().size();

        // Get the reservaSala
        restReservaSalaMockMvc.perform(delete("/api/reserva-salas/{id}", reservaSala.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ReservaSala> reservaSalaList = reservaSalaRepository.findAll();
        assertThat(reservaSalaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReservaSala.class);
        ReservaSala reservaSala1 = new ReservaSala();
        reservaSala1.setId(1L);
        ReservaSala reservaSala2 = new ReservaSala();
        reservaSala2.setId(reservaSala1.getId());
        assertThat(reservaSala1).isEqualTo(reservaSala2);
        reservaSala2.setId(2L);
        assertThat(reservaSala1).isNotEqualTo(reservaSala2);
        reservaSala1.setId(null);
        assertThat(reservaSala1).isNotEqualTo(reservaSala2);
    }
}
