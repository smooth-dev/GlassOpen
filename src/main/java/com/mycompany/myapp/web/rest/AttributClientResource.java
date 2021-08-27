package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.AttributClient;
import com.mycompany.myapp.repository.AttributClientRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.AttributClient}.
 */
@RestController
@RequestMapping("/api")
public class AttributClientResource {

    private final Logger log = LoggerFactory.getLogger(AttributClientResource.class);

    private static final String ENTITY_NAME = "attributClient";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AttributClientRepository attributClientRepository;

    public AttributClientResource(AttributClientRepository attributClientRepository) {
        this.attributClientRepository = attributClientRepository;
    }

    /**
     * {@code POST  /attribut-clients} : Create a new attributClient.
     *
     * @param attributClient the attributClient to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new attributClient, or with status {@code 400 (Bad Request)} if the attributClient has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/attribut-clients")
    public ResponseEntity<AttributClient> createAttributClient(@Valid @RequestBody AttributClient attributClient)
        throws URISyntaxException {
        if (attributClient.getContenu() == null) attributClient.setContenu("test");
        log.debug("REST request to save AttributClient : {}", attributClient);
        if (attributClient.getId() != null) {
            throw new BadRequestAlertException("A new attributClient cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AttributClient result = attributClientRepository.save(attributClient);
        return ResponseEntity
            .created(new URI("/api/attribut-clients/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /attribut-clients/:id} : Updates an existing attributClient.
     *
     * @param id the id of the attributClient to save.
     * @param attributClient the attributClient to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attributClient,
     * or with status {@code 400 (Bad Request)} if the attributClient is not valid,
     * or with status {@code 500 (Internal Server Error)} if the attributClient couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/attribut-clients/{id}")
    public ResponseEntity<AttributClient> updateAttributClient(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody AttributClient attributClient
    ) throws URISyntaxException {
        log.debug("REST request to update AttributClient : {}, {}", id, attributClient);
        if (attributClient.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, attributClient.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!attributClientRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AttributClient result = attributClientRepository.save(attributClient);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, attributClient.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /attribut-clients/:id} : Partial updates given fields of an existing attributClient, field will ignore if it is null
     *
     * @param id the id of the attributClient to save.
     * @param attributClient the attributClient to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attributClient,
     * or with status {@code 400 (Bad Request)} if the attributClient is not valid,
     * or with status {@code 404 (Not Found)} if the attributClient is not found,
     * or with status {@code 500 (Internal Server Error)} if the attributClient couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/attribut-clients/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AttributClient> partialUpdateAttributClient(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody AttributClient attributClient
    ) throws URISyntaxException {
        log.debug("REST request to partial update AttributClient partially : {}, {}", id, attributClient);
        if (attributClient.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, attributClient.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!attributClientRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AttributClient> result = attributClientRepository
            .findById(attributClient.getId())
            .map(
                existingAttributClient -> {
                    if (attributClient.getNomAttribut() != null) {
                        existingAttributClient.setNomAttribut(attributClient.getNomAttribut());
                    }
                    if (attributClient.getContenu() != null) {
                        existingAttributClient.setContenu(attributClient.getContenu());
                    }

                    return existingAttributClient;
                }
            )
            .map(attributClientRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, attributClient.getId())
        );
    }

    /**
     * {@code GET  /attribut-clients} : get all the attributClients.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of attributClients in body.
     */
    @GetMapping("/attribut-clients")
    public List<AttributClient> getAllAttributClients() {
        log.debug("REST request to get all AttributClients");
        return attributClientRepository.findAll();
    }

    /**
     * {@code GET  /attribut-clients/:id} : get the "id" attributClient.
     *
     * @param id the id of the attributClient to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the attributClient, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/attribut-clients/{id}")
    public ResponseEntity<AttributClient> getAttributClient(@PathVariable String id) {
        log.debug("REST request to get AttributClient : {}", id);
        Optional<AttributClient> attributClient = attributClientRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(attributClient);
    }

    /**
     * {@code DELETE  /attribut-clients/:id} : delete the "id" attributClient.
     *
     * @param id the id of the attributClient to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/attribut-clients/{id}")
    public ResponseEntity<Void> deleteAttributClient(@PathVariable String id) {
        log.debug("REST request to delete AttributClient : {}", id);
        attributClientRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
