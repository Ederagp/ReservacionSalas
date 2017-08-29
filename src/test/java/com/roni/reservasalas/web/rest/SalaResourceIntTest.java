package com.roni.reservasalas.web.rest;

import com.roni.reservasalas.ReservaSalasApp;

import com.roni.reservasalas.domain.Sala;
import com.roni.reservasalas.repository.SalaRepository;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SalaResource REST controller.
 *
 * @see SalaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReservaSalasApp.class)
public class SalaResourceIntTest {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_UBICACION = "AAAAAAAAAA";
    private static final String UPDATED_UBICACION = "BBBBBBBBBB";

    private static final String DEFAULT_CAPACIDAD = "AAAAAAAAAA";
    private static final String UPDATED_CAPACIDAD = "BBBBBBBBBB";

    private static final String DEFAULT_EQUIPAMIENTO = "AAAAAAAAAA";
    private static final String UPDATED_EQUIPAMIENTO = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVACIONES = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACIONES = "BBBBBBBBBB";

    @Autowired
    private SalaRepository salaRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSalaMockMvc;

    private Sala sala;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SalaResource salaResource = new SalaResource(salaRepository);
        this.restSalaMockMvc = MockMvcBuilders.standaloneSetup(salaResource)
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
    public static Sala createEntity(EntityManager em) {
        Sala sala = new Sala()
            .nombre(DEFAULT_NOMBRE)
            .ubicacion(DEFAULT_UBICACION)
            .capacidad(DEFAULT_CAPACIDAD)
            .equipamiento(DEFAULT_EQUIPAMIENTO)
            .observaciones(DEFAULT_OBSERVACIONES);
        return sala;
    }

    @Before
    public void initTest() {
        sala = createEntity(em);
    }

    @Test
    @Transactional
    public void createSala() throws Exception {
        int databaseSizeBeforeCreate = salaRepository.findAll().size();

        // Create the Sala
        restSalaMockMvc.perform(post("/api/salas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sala)))
            .andExpect(status().isCreated());

        // Validate the Sala in the database
        List<Sala> salaList = salaRepository.findAll();
        assertThat(salaList).hasSize(databaseSizeBeforeCreate + 1);
        Sala testSala = salaList.get(salaList.size() - 1);
        assertThat(testSala.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testSala.getUbicacion()).isEqualTo(DEFAULT_UBICACION);
        assertThat(testSala.getCapacidad()).isEqualTo(DEFAULT_CAPACIDAD);
        assertThat(testSala.getEquipamiento()).isEqualTo(DEFAULT_EQUIPAMIENTO);
        assertThat(testSala.getObservaciones()).isEqualTo(DEFAULT_OBSERVACIONES);
    }

    @Test
    @Transactional
    public void createSalaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = salaRepository.findAll().size();

        // Create the Sala with an existing ID
        sala.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSalaMockMvc.perform(post("/api/salas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sala)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Sala> salaList = salaRepository.findAll();
        assertThat(salaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSalas() throws Exception {
        // Initialize the database
        salaRepository.saveAndFlush(sala);

        // Get all the salaList
        restSalaMockMvc.perform(get("/api/salas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sala.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
            .andExpect(jsonPath("$.[*].ubicacion").value(hasItem(DEFAULT_UBICACION.toString())))
            .andExpect(jsonPath("$.[*].capacidad").value(hasItem(DEFAULT_CAPACIDAD.toString())))
            .andExpect(jsonPath("$.[*].equipamiento").value(hasItem(DEFAULT_EQUIPAMIENTO.toString())))
            .andExpect(jsonPath("$.[*].observaciones").value(hasItem(DEFAULT_OBSERVACIONES.toString())));
    }

    @Test
    @Transactional
    public void getSala() throws Exception {
        // Initialize the database
        salaRepository.saveAndFlush(sala);

        // Get the sala
        restSalaMockMvc.perform(get("/api/salas/{id}", sala.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sala.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.ubicacion").value(DEFAULT_UBICACION.toString()))
            .andExpect(jsonPath("$.capacidad").value(DEFAULT_CAPACIDAD.toString()))
            .andExpect(jsonPath("$.equipamiento").value(DEFAULT_EQUIPAMIENTO.toString()))
            .andExpect(jsonPath("$.observaciones").value(DEFAULT_OBSERVACIONES.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSala() throws Exception {
        // Get the sala
        restSalaMockMvc.perform(get("/api/salas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSala() throws Exception {
        // Initialize the database
        salaRepository.saveAndFlush(sala);
        int databaseSizeBeforeUpdate = salaRepository.findAll().size();

        // Update the sala
        Sala updatedSala = salaRepository.findOne(sala.getId());
        updatedSala
            .nombre(UPDATED_NOMBRE)
            .ubicacion(UPDATED_UBICACION)
            .capacidad(UPDATED_CAPACIDAD)
            .equipamiento(UPDATED_EQUIPAMIENTO)
            .observaciones(UPDATED_OBSERVACIONES);

        restSalaMockMvc.perform(put("/api/salas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSala)))
            .andExpect(status().isOk());

        // Validate the Sala in the database
        List<Sala> salaList = salaRepository.findAll();
        assertThat(salaList).hasSize(databaseSizeBeforeUpdate);
        Sala testSala = salaList.get(salaList.size() - 1);
        assertThat(testSala.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testSala.getUbicacion()).isEqualTo(UPDATED_UBICACION);
        assertThat(testSala.getCapacidad()).isEqualTo(UPDATED_CAPACIDAD);
        assertThat(testSala.getEquipamiento()).isEqualTo(UPDATED_EQUIPAMIENTO);
        assertThat(testSala.getObservaciones()).isEqualTo(UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    public void updateNonExistingSala() throws Exception {
        int databaseSizeBeforeUpdate = salaRepository.findAll().size();

        // Create the Sala

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSalaMockMvc.perform(put("/api/salas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sala)))
            .andExpect(status().isCreated());

        // Validate the Sala in the database
        List<Sala> salaList = salaRepository.findAll();
        assertThat(salaList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSala() throws Exception {
        // Initialize the database
        salaRepository.saveAndFlush(sala);
        int databaseSizeBeforeDelete = salaRepository.findAll().size();

        // Get the sala
        restSalaMockMvc.perform(delete("/api/salas/{id}", sala.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Sala> salaList = salaRepository.findAll();
        assertThat(salaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sala.class);
        Sala sala1 = new Sala();
        sala1.setId(1L);
        Sala sala2 = new Sala();
        sala2.setId(sala1.getId());
        assertThat(sala1).isEqualTo(sala2);
        sala2.setId(2L);
        assertThat(sala1).isNotEqualTo(sala2);
        sala1.setId(null);
        assertThat(sala1).isNotEqualTo(sala2);
    }
}
