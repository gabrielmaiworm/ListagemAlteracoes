package com.maiworm.alteracao.web.rest;

import com.maiworm.alteracao.domain.Apartamento;
import com.maiworm.alteracao.repository.ApartamentoRepository;
import com.maiworm.alteracao.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.maiworm.alteracao.domain.Apartamento}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ApartamentoResource {

    private final Logger log = LoggerFactory.getLogger(ApartamentoResource.class);

    private static final String ENTITY_NAME = "apartamento";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ApartamentoRepository apartamentoRepository;

    public ApartamentoResource(ApartamentoRepository apartamentoRepository) {
        this.apartamentoRepository = apartamentoRepository;
    }

    /**
     * {@code POST  /apartamentos} : Create a new apartamento.
     *
     * @param apartamento the apartamento to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new apartamento, or with status {@code 400 (Bad Request)} if the apartamento has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/apartamentos")
    public ResponseEntity<Apartamento> createApartamento(@RequestBody Apartamento apartamento) throws URISyntaxException {
        log.debug("REST request to save Apartamento : {}", apartamento);
        if (apartamento.getId() != null) {
            throw new BadRequestAlertException("A new apartamento cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Apartamento result = apartamentoRepository.save(apartamento);
        return ResponseEntity
            .created(new URI("/api/apartamentos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /apartamentos/:id} : Updates an existing apartamento.
     *
     * @param id the id of the apartamento to save.
     * @param apartamento the apartamento to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated apartamento,
     * or with status {@code 400 (Bad Request)} if the apartamento is not valid,
     * or with status {@code 500 (Internal Server Error)} if the apartamento couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/apartamentos/{id}")
    public ResponseEntity<Apartamento> updateApartamento(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Apartamento apartamento
    ) throws URISyntaxException {
        log.debug("REST request to update Apartamento : {}, {}", id, apartamento);
        if (apartamento.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, apartamento.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!apartamentoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Apartamento result = apartamentoRepository.save(apartamento);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, apartamento.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /apartamentos/:id} : Partial updates given fields of an existing apartamento, field will ignore if it is null
     *
     * @param id the id of the apartamento to save.
     * @param apartamento the apartamento to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated apartamento,
     * or with status {@code 400 (Bad Request)} if the apartamento is not valid,
     * or with status {@code 404 (Not Found)} if the apartamento is not found,
     * or with status {@code 500 (Internal Server Error)} if the apartamento couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/apartamentos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Apartamento> partialUpdateApartamento(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Apartamento apartamento
    ) throws URISyntaxException {
        log.debug("REST request to partial update Apartamento partially : {}, {}", id, apartamento);
        if (apartamento.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, apartamento.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!apartamentoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Apartamento> result = apartamentoRepository
            .findById(apartamento.getId())
            .map(existingApartamento -> {
                if (apartamento.getNumero() != null) {
                    existingApartamento.setNumero(apartamento.getNumero());
                }
                if (apartamento.getAla() != null) {
                    existingApartamento.setAla(apartamento.getAla());
                }
                if (apartamento.getResponsavel() != null) {
                    existingApartamento.setResponsavel(apartamento.getResponsavel());
                }
                if (apartamento.getStatus() != null) {
                    existingApartamento.setStatus(apartamento.getStatus());
                }
                if (apartamento.getDetalhes() != null) {
                    existingApartamento.setDetalhes(apartamento.getDetalhes());
                }

                return existingApartamento;
            })
            .map(apartamentoRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, apartamento.getId().toString())
        );
    }

    /**
     * {@code GET  /apartamentos} : get all the apartamentos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of apartamentos in body.
     */
    @GetMapping("/apartamentos")
    public List<Apartamento> getAllApartamentos() {
        log.debug("REST request to get all Apartamentos");
        return apartamentoRepository.findAll();
    }

    /**
     * {@code GET  /apartamentos/:id} : get the "id" apartamento.
     *
     * @param id the id of the apartamento to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the apartamento, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/apartamentos/{id}")
    public ResponseEntity<Apartamento> getApartamento(@PathVariable Long id) {
        log.debug("REST request to get Apartamento : {}", id);
        Optional<Apartamento> apartamento = apartamentoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(apartamento);
    }

    /**
     * {@code DELETE  /apartamentos/:id} : delete the "id" apartamento.
     *
     * @param id the id of the apartamento to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/apartamentos/{id}")
    public ResponseEntity<Void> deleteApartamento(@PathVariable Long id) {
        log.debug("REST request to delete Apartamento : {}", id);
        apartamentoRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
