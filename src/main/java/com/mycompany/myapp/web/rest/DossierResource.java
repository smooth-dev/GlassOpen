package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.AnnexeOut;
import com.mycompany.myapp.domain.Dossier;
import com.mycompany.myapp.domain.Produit;
import com.mycompany.myapp.repository.AnnexeOutRepository;
import com.mycompany.myapp.repository.DossierRepository;
import com.mycompany.myapp.repository.ProduitRepository;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Dossier}.
 */
@RestController
@RequestMapping("/api")
public class DossierResource {

    private final Logger log = LoggerFactory.getLogger(DossierResource.class);

    private static final String ENTITY_NAME = "dossier";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DossierRepository dossierRepository;

    private final ProduitRepository produitRepository;
    private final AnnexeOutRepository annexeOutRepository;

    public DossierResource(
        DossierRepository dossierRepository,
        AnnexeOutRepository annexeOutRepository,
        ProduitRepository produitRepository
    ) {
        this.dossierRepository = dossierRepository;
        this.annexeOutRepository = annexeOutRepository;
        this.produitRepository = produitRepository;
    }

    /**
     * {@code POST  /dossiers} : Create a new dossier.
     *
     * @param dossier the dossier to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dossier, or with status {@code 400 (Bad Request)} if the dossier has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/dossiers")
    public ResponseEntity<Dossier> createDossier(@Valid @RequestBody Dossier dossier) throws URISyntaxException {
        log.debug("REST request to save Dossier : {}", dossier);
        if (dossier.getId() != null) {
            throw new BadRequestAlertException("A new dossier cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Dossier result = dossierRepository.save(dossier);
        return ResponseEntity
            .created(new URI("/api/dossiers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /dossiers/:id} : Updates an existing dossier.
     *
     * @param id the id of the dossier to save.
     * @param dossier the dossier to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dossier,
     * or with status {@code 400 (Bad Request)} if the dossier is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dossier couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/dossiers/{id}")
    public ResponseEntity<Dossier> updateDossier(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody Dossier dossier
    ) throws URISyntaxException {
        log.debug("REST request to update Dossier : {}, {}", id, dossier);
        if (dossier.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dossier.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dossierRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Dossier result = dossierRepository.save(dossier);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dossier.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /dossiers/:id} : Partial updates given fields of an existing dossier, field will ignore if it is null
     *
     * @param id the id of the dossier to save.
     * @param dossier the dossier to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dossier,
     * or with status {@code 400 (Bad Request)} if the dossier is not valid,
     * or with status {@code 404 (Not Found)} if the dossier is not found,
     * or with status {@code 500 (Internal Server Error)} if the dossier couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/dossiers/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Dossier> partialUpdateDossier(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody Dossier dossier
    ) throws URISyntaxException {
        log.debug("REST request to partial update Dossier partially : {}, {}", id, dossier);
        if (dossier.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dossier.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dossierRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Dossier> result = dossierRepository
            .findById(dossier.getId())
            .map(
                existingDossier -> {
                    if (dossier.getNumeroDossier() != null) {
                        existingDossier.setNumeroDossier(dossier.getNumeroDossier());
                    }

                    return existingDossier;
                }
            )
            .map(dossierRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dossier.getId())
        );
    }

    /**
     * {@code GET  /dossiers} : get all the dossiers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dossiers in body.
     */
    @GetMapping("/dossiers")
    public List<Dossier> getAllDossiers() {
        log.debug("REST request to get all Dossiers");
        return dossierRepository.findAll();
    }

    /**
     * {@code GET  /dossiers/:id} : get the "id" dossier.
     *
     * @param id the id of the dossier to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dossier, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/dossiers/{id}")
    public ResponseEntity<Dossier> getDossier(@PathVariable String id) {
        log.debug("REST request to get Dossier : {}", id);
        Optional<Dossier> dossier = dossierRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(dossier);
    }

    @GetMapping("/dossiers/filter/{numeroDossier}")
    public Produit getDossierAnnexeOut(@PathVariable String numeroDossier) {
        log.debug("REST request to get Dossier : {}", numeroDossier);
        Optional<Dossier> dossier = dossierRepository.findAllByNumeroDossier(numeroDossier);
        String nomProduit = dossier.get().getProduit().getNomProduit();

        Produit produits = produitRepository.findByNomProduit(nomProduit);
        log.debug("Pddddddddddd" + produits);

        String id = produits.getId();
        log.debug("IIIIIIIIIIIIIIIIIII" + id);

        Optional<Produit> filtered = produitRepository.findAllById(id);
        Produit prod = filtered.get();
        log.debug("PRODUIT))))))" + prod.getAnnexegroups());
        return prod;
    }

    /**
     * {@code DELETE  /dossiers/:id} : delete the "id" dossier.
     *
     * @param id the id of the dossier to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/dossiers/{id}")
    public ResponseEntity<Void> deleteDossier(@PathVariable String id) {
        log.debug("REST request to delete Dossier : {}", id);
        dossierRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
