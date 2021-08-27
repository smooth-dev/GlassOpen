package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.AnnexeGroup;
import com.mycompany.myapp.repository.AnnexeGroupRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.AnnexeGroup}.
 */
@RestController
@RequestMapping("/api")
public class AnnexeGroupResource {

    private final Logger log = LoggerFactory.getLogger(AnnexeGroupResource.class);

    private static final String ENTITY_NAME = "annexeGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AnnexeGroupRepository annexeGroupRepository;

    public AnnexeGroupResource(AnnexeGroupRepository annexeGroupRepository) {
        this.annexeGroupRepository = annexeGroupRepository;
    }

    /**
     * {@code POST  /annexe-groups} : Create a new annexeGroup.
     *
     * @param annexeGroup the annexeGroup to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new annexeGroup, or with status {@code 400 (Bad Request)} if the annexeGroup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/annexe-groups")
    public ResponseEntity<AnnexeGroup> createAnnexeGroup(@Valid @RequestBody AnnexeGroup annexeGroup) throws URISyntaxException {
        log.debug("REST request to save AnnexeGroup : {}", annexeGroup);
        if (annexeGroup.getId() != null) {
            throw new BadRequestAlertException("A new annexeGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AnnexeGroup result = annexeGroupRepository.save(annexeGroup);
        return ResponseEntity
            .created(new URI("/api/annexe-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /annexe-groups/:id} : Updates an existing annexeGroup.
     *
     * @param id the id of the annexeGroup to save.
     * @param annexeGroup the annexeGroup to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated annexeGroup,
     * or with status {@code 400 (Bad Request)} if the annexeGroup is not valid,
     * or with status {@code 500 (Internal Server Error)} if the annexeGroup couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/annexe-groups/{id}")
    public ResponseEntity<AnnexeGroup> updateAnnexeGroup(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody AnnexeGroup annexeGroup
    ) throws URISyntaxException {
        log.debug("REST request to update AnnexeGroup : {}, {}", id, annexeGroup);
        if (annexeGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, annexeGroup.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!annexeGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AnnexeGroup result = annexeGroupRepository.save(annexeGroup);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, annexeGroup.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /annexe-groups/:id} : Partial updates given fields of an existing annexeGroup, field will ignore if it is null
     *
     * @param id the id of the annexeGroup to save.
     * @param annexeGroup the annexeGroup to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated annexeGroup,
     * or with status {@code 400 (Bad Request)} if the annexeGroup is not valid,
     * or with status {@code 404 (Not Found)} if the annexeGroup is not found,
     * or with status {@code 500 (Internal Server Error)} if the annexeGroup couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/annexe-groups/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AnnexeGroup> partialUpdateAnnexeGroup(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody AnnexeGroup annexeGroup
    ) throws URISyntaxException {
        log.debug("REST request to partial update AnnexeGroup partially : {}, {}", id, annexeGroup);
        if (annexeGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, annexeGroup.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!annexeGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AnnexeGroup> result = annexeGroupRepository
            .findById(annexeGroup.getId())
            .map(
                existingAnnexeGroup -> {
                    if (annexeGroup.getNomAnnexe() != null) {
                        existingAnnexeGroup.setNomAnnexe(annexeGroup.getNomAnnexe());
                    }

                    return existingAnnexeGroup;
                }
            )
            .map(annexeGroupRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, annexeGroup.getId())
        );
    }

    /**
     * {@code GET  /annexe-groups} : get all the annexeGroups.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of annexeGroups in body.
     */
    @GetMapping("/annexe-groups")
    public List<AnnexeGroup> getAllAnnexeGroups() {
        log.debug("REST request to get all AnnexeGroups");
        List<AnnexeGroup> ann = annexeGroupRepository.findAll();
        for (AnnexeGroup grp : ann) {
            log.debug("ss {}", grp.getAnnexeouts());
        }

        return ann;
    }

    /**
     * {@code GET  /annexe-groups/:id} : get the "id" annexeGroup.
     *
     * @param id the id of the annexeGroup to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the annexeGroup, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/annexe-groups/{id}")
    public ResponseEntity<AnnexeGroup> getAnnexeGroup(@PathVariable String id) {
        log.debug("REST request to get AnnexeGroup : {}", id);
        Optional<AnnexeGroup> annexeGroup = annexeGroupRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(annexeGroup);
    }

    /**
     * {@code DELETE  /annexe-groups/:id} : delete the "id" annexeGroup.
     *
     * @param id the id of the annexeGroup to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/annexe-groups/{id}")
    public ResponseEntity<Void> deleteAnnexeGroup(@PathVariable String id) {
        log.debug("REST request to delete AnnexeGroup : {}", id);
        annexeGroupRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
