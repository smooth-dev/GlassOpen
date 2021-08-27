package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.AttributOut;
import com.mycompany.myapp.repository.AttributOutRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.AttributOut}.
 */
@RestController
@RequestMapping("/api")
public class AttributOutResource {

    private final Logger log = LoggerFactory.getLogger(AttributOutResource.class);

    private static final String ENTITY_NAME = "attributOut";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AttributOutRepository attributOutRepository;

    public AttributOutResource(AttributOutRepository attributOutRepository) {
        this.attributOutRepository = attributOutRepository;
    }

    /**
     * {@code POST  /attribut-outs} : Create a new attributOut.
     *
     * @param attributOut the attributOut to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new attributOut, or with status {@code 400 (Bad Request)} if the attributOut has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/attribut-outs")
    public ResponseEntity<AttributOut> createAttributOut(@Valid @RequestBody AttributOut attributOut) throws URISyntaxException {
        log.debug("REST request to save AttributOut : {}", attributOut);
        if (attributOut.getId() != null) {
            throw new BadRequestAlertException("A new attributOut cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AttributOut result = attributOutRepository.save(attributOut);
        return ResponseEntity
            .created(new URI("/api/attribut-outs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /attribut-outs/:id} : Updates an existing attributOut.
     *
     * @param id the id of the attributOut to save.
     * @param attributOut the attributOut to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attributOut,
     * or with status {@code 400 (Bad Request)} if the attributOut is not valid,
     * or with status {@code 500 (Internal Server Error)} if the attributOut couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/attribut-outs/{id}")
    public ResponseEntity<AttributOut> updateAttributOut(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody AttributOut attributOut
    ) throws URISyntaxException {
        log.debug("REST request to update AttributOut : {}, {}", id, attributOut);
        if (attributOut.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, attributOut.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!attributOutRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AttributOut result = attributOutRepository.save(attributOut);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, attributOut.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /attribut-outs/:id} : Partial updates given fields of an existing attributOut, field will ignore if it is null
     *
     * @param id the id of the attributOut to save.
     * @param attributOut the attributOut to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attributOut,
     * or with status {@code 400 (Bad Request)} if the attributOut is not valid,
     * or with status {@code 404 (Not Found)} if the attributOut is not found,
     * or with status {@code 500 (Internal Server Error)} if the attributOut couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/attribut-outs/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AttributOut> partialUpdateAttributOut(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody AttributOut attributOut
    ) throws URISyntaxException {
        log.debug("REST request to partial update AttributOut partially : {}, {}", id, attributOut);
        if (attributOut.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, attributOut.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!attributOutRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AttributOut> result = attributOutRepository
            .findById(attributOut.getId())
            .map(
                existingAttributOut -> {
                    if (attributOut.getNomAttribut() != null) {
                        existingAttributOut.setNomAttribut(attributOut.getNomAttribut());
                    }

                    return existingAttributOut;
                }
            )
            .map(attributOutRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, attributOut.getId())
        );
    }

    /**
     * {@code GET  /attribut-outs} : get all the attributOuts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of attributOuts in body.
     */
    @GetMapping("/attribut-outs")
    public List<AttributOut> getAllAttributOuts() {
        log.debug("REST request to get all AttributOuts");
        return attributOutRepository.findAll();
    }

    /**
     * {@code GET  /attribut-outs/:id} : get the "id" attributOut.
     *
     * @param id the id of the attributOut to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the attributOut, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/attribut-outs/{id}")
    public ResponseEntity<AttributOut> getAttributOut(@PathVariable String id) {
        log.debug("REST request to get AttributOut : {}", id);
        Optional<AttributOut> attributOut = attributOutRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(attributOut);
    }

    /**
     * {@code DELETE  /attribut-outs/:id} : delete the "id" attributOut.
     *
     * @param id the id of the attributOut to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/attribut-outs/{id}")
    public ResponseEntity<Void> deleteAttributOut(@PathVariable String id) {
        log.debug("REST request to delete AttributOut : {}", id);
        attributOutRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
