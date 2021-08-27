package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.ProduitOut;
import com.mycompany.myapp.repository.ProduitOutRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.ProduitOut}.
 */
@RestController
@RequestMapping("/api")
public class ProduitOutResource {

    private final Logger log = LoggerFactory.getLogger(ProduitOutResource.class);

    private static final String ENTITY_NAME = "produitOut";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProduitOutRepository produitOutRepository;

    public ProduitOutResource(ProduitOutRepository produitOutRepository) {
        this.produitOutRepository = produitOutRepository;
    }

    /**
     * {@code POST  /produit-outs} : Create a new produitOut.
     *
     * @param produitOut the produitOut to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new produitOut, or with status {@code 400 (Bad Request)} if the produitOut has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/produit-outs")
    public ResponseEntity<ProduitOut> createProduitOut(@Valid @RequestBody ProduitOut produitOut) throws URISyntaxException {
        log.debug("REST request to save ProduitOut : {}", produitOut);
        if (produitOut.getId() != null) {
            throw new BadRequestAlertException("A new produitOut cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProduitOut result = produitOutRepository.save(produitOut);
        return ResponseEntity
            .created(new URI("/api/produit-outs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /produit-outs/:id} : Updates an existing produitOut.
     *
     * @param id the id of the produitOut to save.
     * @param produitOut the produitOut to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated produitOut,
     * or with status {@code 400 (Bad Request)} if the produitOut is not valid,
     * or with status {@code 500 (Internal Server Error)} if the produitOut couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/produit-outs/{id}")
    public ResponseEntity<ProduitOut> updateProduitOut(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody ProduitOut produitOut
    ) throws URISyntaxException {
        log.debug("REST request to update ProduitOut : {}, {}", id, produitOut);
        if (produitOut.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, produitOut.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!produitOutRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProduitOut result = produitOutRepository.save(produitOut);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, produitOut.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /produit-outs/:id} : Partial updates given fields of an existing produitOut, field will ignore if it is null
     *
     * @param id the id of the produitOut to save.
     * @param produitOut the produitOut to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated produitOut,
     * or with status {@code 400 (Bad Request)} if the produitOut is not valid,
     * or with status {@code 404 (Not Found)} if the produitOut is not found,
     * or with status {@code 500 (Internal Server Error)} if the produitOut couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/produit-outs/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ProduitOut> partialUpdateProduitOut(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody ProduitOut produitOut
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProduitOut partially : {}, {}", id, produitOut);
        if (produitOut.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, produitOut.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!produitOutRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProduitOut> result = produitOutRepository
            .findById(produitOut.getId())
            .map(
                existingProduitOut -> {
                    if (produitOut.getNomAnnexe() != null) {
                        existingProduitOut.setNomAnnexe(produitOut.getNomAnnexe());
                    }

                    return existingProduitOut;
                }
            )
            .map(produitOutRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, produitOut.getId())
        );
    }

    /**
     * {@code GET  /produit-outs} : get all the produitOuts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of produitOuts in body.
     */
    @GetMapping("/produit-outs")
    public List<ProduitOut> getAllProduitOuts() {
        log.debug("REST request to get all ProduitOuts");
        return produitOutRepository.findAll();
    }

    /**
     * {@code GET  /produit-outs/:id} : get the "id" produitOut.
     *
     * @param id the id of the produitOut to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the produitOut, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/produit-outs/{id}")
    public ResponseEntity<ProduitOut> getProduitOut(@PathVariable String id) {
        log.debug("REST request to get ProduitOut : {}", id);
        Optional<ProduitOut> produitOut = produitOutRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(produitOut);
    }

    /**
     * {@code DELETE  /produit-outs/:id} : delete the "id" produitOut.
     *
     * @param id the id of the produitOut to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/produit-outs/{id}")
    public ResponseEntity<Void> deleteProduitOut(@PathVariable String id) {
        log.debug("REST request to delete ProduitOut : {}", id);
        produitOutRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
