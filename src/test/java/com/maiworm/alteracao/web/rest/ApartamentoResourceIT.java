package com.maiworm.alteracao.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.maiworm.alteracao.IntegrationTest;
import com.maiworm.alteracao.domain.Apartamento;
import com.maiworm.alteracao.domain.enumeration.Ala;
import com.maiworm.alteracao.domain.enumeration.Status;
import com.maiworm.alteracao.repository.ApartamentoRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ApartamentoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ApartamentoResourceIT {

    private static final Integer DEFAULT_NUMERO = 1;
    private static final Integer UPDATED_NUMERO = 2;

    private static final Ala DEFAULT_ALA = Ala.PAR;
    private static final Ala UPDATED_ALA = Ala.IMPAR;

    private static final String DEFAULT_RESPONSAVEL = "AAAAAAAAAA";
    private static final String UPDATED_RESPONSAVEL = "BBBBBBBBBB";

    private static final Status DEFAULT_STATUS = Status.PRONTO;
    private static final Status UPDATED_STATUS = Status.OCUPADO;

    private static final String DEFAULT_DETALHES = "AAAAAAAAAA";
    private static final String UPDATED_DETALHES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/apartamentos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ApartamentoRepository apartamentoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApartamentoMockMvc;

    private Apartamento apartamento;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Apartamento createEntity(EntityManager em) {
        Apartamento apartamento = new Apartamento()
            .numero(DEFAULT_NUMERO)
            .ala(DEFAULT_ALA)
            .responsavel(DEFAULT_RESPONSAVEL)
            .status(DEFAULT_STATUS)
            .detalhes(DEFAULT_DETALHES);
        return apartamento;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Apartamento createUpdatedEntity(EntityManager em) {
        Apartamento apartamento = new Apartamento()
            .numero(UPDATED_NUMERO)
            .ala(UPDATED_ALA)
            .responsavel(UPDATED_RESPONSAVEL)
            .status(UPDATED_STATUS)
            .detalhes(UPDATED_DETALHES);
        return apartamento;
    }

    @BeforeEach
    public void initTest() {
        apartamento = createEntity(em);
    }

    @Test
    @Transactional
    void createApartamento() throws Exception {
        int databaseSizeBeforeCreate = apartamentoRepository.findAll().size();
        // Create the Apartamento
        restApartamentoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apartamento)))
            .andExpect(status().isCreated());

        // Validate the Apartamento in the database
        List<Apartamento> apartamentoList = apartamentoRepository.findAll();
        assertThat(apartamentoList).hasSize(databaseSizeBeforeCreate + 1);
        Apartamento testApartamento = apartamentoList.get(apartamentoList.size() - 1);
        assertThat(testApartamento.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testApartamento.getAla()).isEqualTo(DEFAULT_ALA);
        assertThat(testApartamento.getResponsavel()).isEqualTo(DEFAULT_RESPONSAVEL);
        assertThat(testApartamento.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testApartamento.getDetalhes()).isEqualTo(DEFAULT_DETALHES);
    }

    @Test
    @Transactional
    void createApartamentoWithExistingId() throws Exception {
        // Create the Apartamento with an existing ID
        apartamento.setId(1L);

        int databaseSizeBeforeCreate = apartamentoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restApartamentoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apartamento)))
            .andExpect(status().isBadRequest());

        // Validate the Apartamento in the database
        List<Apartamento> apartamentoList = apartamentoRepository.findAll();
        assertThat(apartamentoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllApartamentos() throws Exception {
        // Initialize the database
        apartamentoRepository.saveAndFlush(apartamento);

        // Get all the apartamentoList
        restApartamentoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(apartamento.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].ala").value(hasItem(DEFAULT_ALA.toString())))
            .andExpect(jsonPath("$.[*].responsavel").value(hasItem(DEFAULT_RESPONSAVEL)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].detalhes").value(hasItem(DEFAULT_DETALHES)));
    }

    @Test
    @Transactional
    void getApartamento() throws Exception {
        // Initialize the database
        apartamentoRepository.saveAndFlush(apartamento);

        // Get the apartamento
        restApartamentoMockMvc
            .perform(get(ENTITY_API_URL_ID, apartamento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(apartamento.getId().intValue()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.ala").value(DEFAULT_ALA.toString()))
            .andExpect(jsonPath("$.responsavel").value(DEFAULT_RESPONSAVEL))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.detalhes").value(DEFAULT_DETALHES));
    }

    @Test
    @Transactional
    void getNonExistingApartamento() throws Exception {
        // Get the apartamento
        restApartamentoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewApartamento() throws Exception {
        // Initialize the database
        apartamentoRepository.saveAndFlush(apartamento);

        int databaseSizeBeforeUpdate = apartamentoRepository.findAll().size();

        // Update the apartamento
        Apartamento updatedApartamento = apartamentoRepository.findById(apartamento.getId()).get();
        // Disconnect from session so that the updates on updatedApartamento are not directly saved in db
        em.detach(updatedApartamento);
        updatedApartamento
            .numero(UPDATED_NUMERO)
            .ala(UPDATED_ALA)
            .responsavel(UPDATED_RESPONSAVEL)
            .status(UPDATED_STATUS)
            .detalhes(UPDATED_DETALHES);

        restApartamentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedApartamento.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedApartamento))
            )
            .andExpect(status().isOk());

        // Validate the Apartamento in the database
        List<Apartamento> apartamentoList = apartamentoRepository.findAll();
        assertThat(apartamentoList).hasSize(databaseSizeBeforeUpdate);
        Apartamento testApartamento = apartamentoList.get(apartamentoList.size() - 1);
        assertThat(testApartamento.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testApartamento.getAla()).isEqualTo(UPDATED_ALA);
        assertThat(testApartamento.getResponsavel()).isEqualTo(UPDATED_RESPONSAVEL);
        assertThat(testApartamento.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testApartamento.getDetalhes()).isEqualTo(UPDATED_DETALHES);
    }

    @Test
    @Transactional
    void putNonExistingApartamento() throws Exception {
        int databaseSizeBeforeUpdate = apartamentoRepository.findAll().size();
        apartamento.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApartamentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, apartamento.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(apartamento))
            )
            .andExpect(status().isBadRequest());

        // Validate the Apartamento in the database
        List<Apartamento> apartamentoList = apartamentoRepository.findAll();
        assertThat(apartamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchApartamento() throws Exception {
        int databaseSizeBeforeUpdate = apartamentoRepository.findAll().size();
        apartamento.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApartamentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(apartamento))
            )
            .andExpect(status().isBadRequest());

        // Validate the Apartamento in the database
        List<Apartamento> apartamentoList = apartamentoRepository.findAll();
        assertThat(apartamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamApartamento() throws Exception {
        int databaseSizeBeforeUpdate = apartamentoRepository.findAll().size();
        apartamento.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApartamentoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apartamento)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Apartamento in the database
        List<Apartamento> apartamentoList = apartamentoRepository.findAll();
        assertThat(apartamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateApartamentoWithPatch() throws Exception {
        // Initialize the database
        apartamentoRepository.saveAndFlush(apartamento);

        int databaseSizeBeforeUpdate = apartamentoRepository.findAll().size();

        // Update the apartamento using partial update
        Apartamento partialUpdatedApartamento = new Apartamento();
        partialUpdatedApartamento.setId(apartamento.getId());

        partialUpdatedApartamento.numero(UPDATED_NUMERO).ala(UPDATED_ALA).responsavel(UPDATED_RESPONSAVEL);

        restApartamentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApartamento.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApartamento))
            )
            .andExpect(status().isOk());

        // Validate the Apartamento in the database
        List<Apartamento> apartamentoList = apartamentoRepository.findAll();
        assertThat(apartamentoList).hasSize(databaseSizeBeforeUpdate);
        Apartamento testApartamento = apartamentoList.get(apartamentoList.size() - 1);
        assertThat(testApartamento.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testApartamento.getAla()).isEqualTo(UPDATED_ALA);
        assertThat(testApartamento.getResponsavel()).isEqualTo(UPDATED_RESPONSAVEL);
        assertThat(testApartamento.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testApartamento.getDetalhes()).isEqualTo(DEFAULT_DETALHES);
    }

    @Test
    @Transactional
    void fullUpdateApartamentoWithPatch() throws Exception {
        // Initialize the database
        apartamentoRepository.saveAndFlush(apartamento);

        int databaseSizeBeforeUpdate = apartamentoRepository.findAll().size();

        // Update the apartamento using partial update
        Apartamento partialUpdatedApartamento = new Apartamento();
        partialUpdatedApartamento.setId(apartamento.getId());

        partialUpdatedApartamento
            .numero(UPDATED_NUMERO)
            .ala(UPDATED_ALA)
            .responsavel(UPDATED_RESPONSAVEL)
            .status(UPDATED_STATUS)
            .detalhes(UPDATED_DETALHES);

        restApartamentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApartamento.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApartamento))
            )
            .andExpect(status().isOk());

        // Validate the Apartamento in the database
        List<Apartamento> apartamentoList = apartamentoRepository.findAll();
        assertThat(apartamentoList).hasSize(databaseSizeBeforeUpdate);
        Apartamento testApartamento = apartamentoList.get(apartamentoList.size() - 1);
        assertThat(testApartamento.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testApartamento.getAla()).isEqualTo(UPDATED_ALA);
        assertThat(testApartamento.getResponsavel()).isEqualTo(UPDATED_RESPONSAVEL);
        assertThat(testApartamento.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testApartamento.getDetalhes()).isEqualTo(UPDATED_DETALHES);
    }

    @Test
    @Transactional
    void patchNonExistingApartamento() throws Exception {
        int databaseSizeBeforeUpdate = apartamentoRepository.findAll().size();
        apartamento.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApartamentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, apartamento.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(apartamento))
            )
            .andExpect(status().isBadRequest());

        // Validate the Apartamento in the database
        List<Apartamento> apartamentoList = apartamentoRepository.findAll();
        assertThat(apartamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchApartamento() throws Exception {
        int databaseSizeBeforeUpdate = apartamentoRepository.findAll().size();
        apartamento.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApartamentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(apartamento))
            )
            .andExpect(status().isBadRequest());

        // Validate the Apartamento in the database
        List<Apartamento> apartamentoList = apartamentoRepository.findAll();
        assertThat(apartamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamApartamento() throws Exception {
        int databaseSizeBeforeUpdate = apartamentoRepository.findAll().size();
        apartamento.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApartamentoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(apartamento))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Apartamento in the database
        List<Apartamento> apartamentoList = apartamentoRepository.findAll();
        assertThat(apartamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteApartamento() throws Exception {
        // Initialize the database
        apartamentoRepository.saveAndFlush(apartamento);

        int databaseSizeBeforeDelete = apartamentoRepository.findAll().size();

        // Delete the apartamento
        restApartamentoMockMvc
            .perform(delete(ENTITY_API_URL_ID, apartamento.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Apartamento> apartamentoList = apartamentoRepository.findAll();
        assertThat(apartamentoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
