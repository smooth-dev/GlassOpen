package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Attribut;
import com.mycompany.myapp.repository.AttributRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Attribut}.
 */
@RestController
@RequestMapping("/api")
public class AttributResource {

    private final Logger log = LoggerFactory.getLogger(AttributResource.class);

    private static final String ENTITY_NAME = "attribut";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AttributRepository attributRepository;

    public AttributResource(AttributRepository attributRepository) {
        this.attributRepository = attributRepository;
    }

    /**
     * {@code POST  /attributs} : Create a new attribut.
     *
     * @param attribut the attribut to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new attribut, or with status {@code 400 (Bad Request)} if the attribut has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/attributs")
    public ResponseEntity<Attribut> createAttribut(@Valid @RequestBody Attribut attribut) throws URISyntaxException {
        log.debug("REST request to save Attribut : {}", attribut);
        if (attribut.getId() != null) {
            throw new BadRequestAlertException("A new attribut cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Attribut result = attributRepository.save(attribut);
        return ResponseEntity
            .created(new URI("/api/attributs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /attributs/:id} : Updates an existing attribut.
     *
     * @param id the id of the attribut to save.
     * @param attribut the attribut to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attribut,
     * or with status {@code 400 (Bad Request)} if the attribut is not valid,
     * or with status {@code 500 (Internal Server Error)} if the attribut couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/attributs/{id}")
    public ResponseEntity<Attribut> updateAttribut(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody Attribut attribut
    ) throws URISyntaxException {
        log.debug("REST request to update Attribut : {}, {}", id, attribut);
        if (attribut.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, attribut.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!attributRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Attribut result = attributRepository.save(attribut);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, attribut.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /attributs/:id} : Partial updates given fields of an existing attribut, field will ignore if it is null
     *
     * @param id the id of the attribut to save.
     * @param attribut the attribut to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attribut,
     * or with status {@code 400 (Bad Request)} if the attribut is not valid,
     * or with status {@code 404 (Not Found)} if the attribut is not found,
     * or with status {@code 500 (Internal Server Error)} if the attribut couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/attributs/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Attribut> partialUpdateAttribut(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody Attribut attribut
    ) throws URISyntaxException {
        log.debug("REST request to partial update Attribut partially : {}, {}", id, attribut);
        if (attribut.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, attribut.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!attributRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Attribut> result = attributRepository
            .findById(attribut.getId())
            .map(
                existingAttribut -> {
                    if (attribut.getNomAttribut() != null) {
                        existingAttribut.setNomAttribut(attribut.getNomAttribut());
                    }

                    return existingAttribut;
                }
            )
            .map(attributRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, attribut.getId())
        );
    }

    /**
     * {@code GET  /attributs} : get all the attributs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of attributs in body.
     */
    @GetMapping("/attributs")
    public List<Attribut> getAllAttributs() {
        log.debug("REST request to get all Attributs");
        return attributRepository.findAll();
    }

    /**
     * {@code GET  /attributs/:id} : get the "id" attribut.
     *
     * @param id the id of the attribut to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the attribut, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/attributs/{id}")
    public ResponseEntity<Attribut> getAttribut(@PathVariable String id) {
        log.debug("REST request to get Attribut : {}", id);
        Optional<Attribut> attribut = attributRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(attribut);
    }

    /**
     * {@code DELETE  /attributs/:id} : delete the "id" attribut.
     *
     * @param id the id of the attribut to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/attributs/{id}")
    public ResponseEntity<Void> deleteAttribut(@PathVariable String id) {
        log.debug("REST request to delete Attribut : {}", id);
        attributRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
