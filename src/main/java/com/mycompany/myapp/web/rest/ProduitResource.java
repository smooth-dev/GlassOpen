package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Annexe;
import com.mycompany.myapp.domain.AnnexeOut;
import com.mycompany.myapp.domain.Produit;
import com.mycompany.myapp.repository.AnnexeOutRepository;
import com.mycompany.myapp.repository.AnnexeRepository;
import com.mycompany.myapp.repository.ProduitRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Produit}.
 */
@RestController
@RequestMapping("/api")
public class ProduitResource {

    private final Logger log = LoggerFactory.getLogger(ProduitResource.class);

    private static final String ENTITY_NAME = "produit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProduitRepository produitRepository;
    private final AnnexeRepository annexeRepository;
    private final AnnexeOutRepository annexeOutRepository;

    public ProduitResource(
        ProduitRepository produitRepository,
        AnnexeRepository annexeRepository,
        AnnexeOutRepository annexeOutRepository
    ) {
        this.produitRepository = produitRepository;
        this.annexeRepository = annexeRepository;
        this.annexeOutRepository = annexeOutRepository;
    }

    /**
     * {@code POST  /produits} : Create a new produit.
     *
     * @param produit the produit to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new produit, or with status {@code 400 (Bad Request)} if the produit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/produits")
    public ResponseEntity<Produit> createProduit(@Valid @RequestBody Produit produit) throws URISyntaxException {
        log.debug("REST request to save Produit : {}", produit);
        if (produit.getId() != null) {
            throw new BadRequestAlertException("A new produit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Produit result = produitRepository.save(produit);
        return ResponseEntity
            .created(new URI("/api/produits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /produits/:id} : Updates an existing produit.
     *
     * @param id the id of the produit to save.
     * @param produit the produit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated produit,
     * or with status {@code 400 (Bad Request)} if the produit is not valid,
     * or with status {@code 500 (Internal Server Error)} if the produit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/produits/{id}")
    public ResponseEntity<Produit> updateProduit(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody Produit produit
    ) throws URISyntaxException {
        log.debug("REST request to update Produit : {}, {}", id, produit);
        if (produit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, produit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!produitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Produit result = produitRepository.save(produit);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, produit.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /produits/:id} : Partial updates given fields of an existing produit, field will ignore if it is null
     *
     * @param id the id of the produit to save.
     * @param produit the produit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated produit,
     * or with status {@code 400 (Bad Request)} if the produit is not valid,
     * or with status {@code 404 (Not Found)} if the produit is not found,
     * or with status {@code 500 (Internal Server Error)} if the produit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/produits/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Produit> partialUpdateProduit(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody Produit produit
    ) throws URISyntaxException {
        log.debug("REST request to partial update Produit partially : {}, {}", id, produit);
        if (produit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, produit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!produitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Produit> result = produitRepository
            .findById(produit.getId())
            .map(
                existingProduit -> {
                    if (produit.getNomProduit() != null) {
                        existingProduit.setNomProduit(produit.getNomProduit());
                    }

                    return existingProduit;
                }
            )
            .map(produitRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, produit.getId())
        );
    }

    /**
     * {@code GET  /produits} : get all the produits.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of produits in body.
     */
    @GetMapping("/produits")
    public List<Produit> getAllProduits() {
        log.debug("REST request to get all Produits");
        return produitRepository.findAll();
    }

    /**
     * {@code GET  /produits/:id} : get the "id" produit.
     *
     * @param id the id of the produit to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the produit, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/produits/{id}")
    public ResponseEntity<Produit> getProduit(@PathVariable String id) {
        log.debug("REST request to get Produit : {}", id);
        Optional<Produit> produit = produitRepository.findById(id);
        log.debug("ooooooooo" + produit.get().getNomProduit());

        //        List<String> attribut = new ArrayList<String>();
        //
        //        List<Annexe> annexes = annexeRepository.findAllById(id);
        //        for(Annexe annexe : annexes)
        //        {        log.debug("YYYYYYYYYYYYY: {}", annexe.getId());
        //
        //          AnnexeResource.generateAnnexe(annexe,id);}
        return ResponseUtil.wrapOrNotFound(produit);
    }

    @GetMapping("/produits/generate/{id}")
    public ResponseEntity<Produit> getProduitGenerate(@PathVariable String id) {
        log.debug("REST request to get Produit : {}", id);
        Optional<Produit> produit = produitRepository.findById(id);
        //        List<String> attribut = new ArrayList<String>();

        List<Annexe> annexes = annexeRepository.findAllById(id);
        for (Annexe annexe : annexes) {
            log.debug("YYYYYYYYYYYYY: {}", annexe.getId());

            AnnexeResource.generateAnnexe(annexe, produit.get());
        }
        return ResponseUtil.wrapOrNotFound(produit);
    }

    @GetMapping("/produits/filter/{nomProduit}")
    public List<AnnexeOut> getAnnexeOutByProduit(@PathVariable String nomProduit) {
        log.debug("REST request to get AnnexeOut : {}", nomProduit);

        Produit produits = produitRepository.findByNomProduit(nomProduit);
        String id = produits.getId();

        List<AnnexeOut> annexesout = annexeOutRepository.findAllById(id);
        log.debug("PRODUIT))))))" + annexesout);

        return annexesout;
    }

    /**
     * {@code DELETE  /produits/:id} : delete the "id" produit.
     *
     * @param id the id of the produit to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/produits/{id}")
    public ResponseEntity<Void> deleteProduit(@PathVariable String id) {
        log.debug("REST request to delete Produit : {}", id);
        produitRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
