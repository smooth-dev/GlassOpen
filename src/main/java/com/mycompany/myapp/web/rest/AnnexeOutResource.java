package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.AnnexeOut;
import com.mycompany.myapp.repository.AnnexeOutRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.AnnexeOut}.
 */
@RestController
@RequestMapping("/api")
public class AnnexeOutResource {

    private final Logger log = LoggerFactory.getLogger(AnnexeOutResource.class);

    private static final String ENTITY_NAME = "annexeOut";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AnnexeOutRepository annexeOutRepository;

    public AnnexeOutResource(AnnexeOutRepository annexeOutRepository) {
        this.annexeOutRepository = annexeOutRepository;
    }

    /**
     * {@code POST  /annexe-outs} : Create a new annexeOut.
     *
     * @param annexeOut the annexeOut to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new annexeOut, or with status {@code 400 (Bad Request)} if the annexeOut has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/annexe-outs")
    public ResponseEntity<AnnexeOut> createAnnexeOut(@Valid @RequestBody AnnexeOut annexeOut) throws URISyntaxException {
        log.debug("REST request to save AnnexeOut : {}", annexeOut);
        if (annexeOut.getId() != null) {
            throw new BadRequestAlertException("A new annexeOut cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AnnexeOut result = annexeOutRepository.save(annexeOut);
        return ResponseEntity
            .created(new URI("/api/annexe-outs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /annexe-outs/:id} : Updates an existing annexeOut.
     *
     * @param id the id of the annexeOut to save.
     * @param annexeOut the annexeOut to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated annexeOut,
     * or with status {@code 400 (Bad Request)} if the annexeOut is not valid,
     * or with status {@code 500 (Internal Server Error)} if the annexeOut couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/annexe-outs/{id}")
    public ResponseEntity<AnnexeOut> updateAnnexeOut(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody AnnexeOut annexeOut
    ) throws URISyntaxException {
        log.debug("REST request to update AnnexeOut : {}, {}", id, annexeOut);
        if (annexeOut.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, annexeOut.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!annexeOutRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AnnexeOut result = annexeOutRepository.save(annexeOut);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, annexeOut.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /annexe-outs/:id} : Partial updates given fields of an existing annexeOut, field will ignore if it is null
     *
     * @param id the id of the annexeOut to save.
     * @param annexeOut the annexeOut to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated annexeOut,
     * or with status {@code 400 (Bad Request)} if the annexeOut is not valid,
     * or with status {@code 404 (Not Found)} if the annexeOut is not found,
     * or with status {@code 500 (Internal Server Error)} if the annexeOut couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/annexe-outs/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AnnexeOut> partialUpdateAnnexeOut(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody AnnexeOut annexeOut
    ) throws URISyntaxException {
        log.debug("REST request to partial update AnnexeOut partially : {}, {}", id, annexeOut);
        if (annexeOut.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, annexeOut.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!annexeOutRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AnnexeOut> result = annexeOutRepository
            .findById(annexeOut.getId())
            .map(
                existingAnnexeOut -> {
                    if (annexeOut.getNomAnnexe() != null) {
                        existingAnnexeOut.setNomAnnexe(annexeOut.getNomAnnexe());
                    }
                    if (annexeOut.getIdClient() != null) {
                        existingAnnexeOut.setIdClient(annexeOut.getIdClient());
                    }

                    return existingAnnexeOut;
                }
            )
            .map(annexeOutRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, annexeOut.getId())
        );
    }

    /**
     * {@code GET  /annexe-outs} : get all the annexeOuts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of annexeOuts in body.
     */
    @GetMapping("/annexe-outs")
    public List<AnnexeOut> getAllAnnexeOuts() {
        log.debug("REST request to get all AnnexeOuts");
        return annexeOutRepository.findAll();
    }

    /**
     * {@code GET  /annexe-outs/:id} : get the "id" annexeOut.
     *
     * @param id the id of the annexeOut to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the annexeOut, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/annexe-outs/{id}")
    public ResponseEntity<AnnexeOut> getAnnexeOut(@PathVariable String id) {
        log.debug("REST request to get AnnexeOut : {}", id);
        Optional<AnnexeOut> annexeOut = annexeOutRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(annexeOut);
    }

    /**
     * {@code DELETE  /annexe-outs/:id} : delete the "id" annexeOut.
     *
     * @param id the id of the annexeOut to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/annexe-outs/{id}")
    public ResponseEntity<Void> deleteAnnexeOut(@PathVariable String id) {
        log.debug("REST request to delete AnnexeOut : {}", id);
        annexeOutRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
