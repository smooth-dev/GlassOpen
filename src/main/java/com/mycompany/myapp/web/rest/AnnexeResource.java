package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Annexe;
import com.mycompany.myapp.domain.AnnexeGroup;
import com.mycompany.myapp.domain.AnnexeOut;
import com.mycompany.myapp.domain.Attribut;
import com.mycompany.myapp.domain.AttributClient;
import com.mycompany.myapp.domain.Client;
import com.mycompany.myapp.domain.Dossier;
import com.mycompany.myapp.domain.Produit;
import com.mycompany.myapp.repository.AnnexeGroupRepository;
import com.mycompany.myapp.repository.AnnexeOutRepository;
import com.mycompany.myapp.repository.AnnexeRepository;
import com.mycompany.myapp.repository.AttributClientRepository;
import com.mycompany.myapp.repository.AttributRepository;
import com.mycompany.myapp.repository.ClientRepository;
import com.mycompany.myapp.repository.DossierRepository;
import com.mycompany.myapp.repository.ProduitRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Annexe}.
 */
@RestController
@RequestMapping("/api")
public class AnnexeResource {

    private static Logger log = LoggerFactory.getLogger(AnnexeResource.class);

    private static final String ENTITY_NAME = "annexe";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private static AnnexeRepository annexeRepository;
    private static AnnexeOutRepository annexeoutRepository;
    private static AttributRepository attributRepository;
    private static AttributClientRepository attributClientRepository;
    private static ClientRepository clientRepository;
    private static DossierRepository dossierRepository;
    private static AnnexeGroupRepository annexeGroupRepository;
    private static ProduitRepository produitRepository;

    public AnnexeResource(
        AnnexeRepository annexeRepository,
        AnnexeOutRepository annexeoutRepository,
        AttributRepository attributRepository,
        AttributClientRepository attributClientRepository,
        ClientRepository clientRepository,
        AnnexeGroupRepository annexeGroupRepository,
        DossierRepository dossierRepository,
        ProduitRepository produitRepository
    ) {
        this.annexeRepository = annexeRepository;
        this.annexeoutRepository = annexeoutRepository;
        this.attributRepository = attributRepository;
        this.attributClientRepository = attributClientRepository;
        this.clientRepository = clientRepository;
        this.annexeGroupRepository = annexeGroupRepository;
        this.dossierRepository = dossierRepository;
        this.produitRepository = produitRepository;
    }

    /**
     * {@code POST  /annexes} : Create a new annexe.
     *
     * @param annexe the annexe to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new annexe, or with status {@code 400 (Bad Request)} if the annexe has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/annexes")
    public ResponseEntity<Annexe> createAnnexe(@Valid @RequestBody Annexe annexe) throws URISyntaxException {
        log.debug("REST request to save Annexe : {}", annexe);
        if (annexe.getId() != null) {
            throw new BadRequestAlertException("A new annexe cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Annexe result = annexeRepository.save(annexe);
        return ResponseEntity
            .created(new URI("/api/annexes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /annexes/:id} : Updates an existing annexe.
     *
     * @param id the id of the annexe to save.
     * @param annexe the annexe to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated annexe,
     * or with status {@code 400 (Bad Request)} if the annexe is not valid,
     * or with status {@code 500 (Internal Server Error)} if the annexe couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/annexes/{id}")
    public ResponseEntity<Annexe> updateAnnexe(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody Annexe annexe
    ) throws URISyntaxException {
        log.debug("REST request to update Annexe : {}, {}", id, annexe);
        if (annexe.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, annexe.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!annexeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Annexe result = annexeRepository.save(annexe);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, annexe.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /annexes/:id} : Partial updates given fields of an existing annexe, field will ignore if it is null
     *
     * @param id the id of the annexe to save.
     * @param annexe the annexe to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated annexe,
     * or with status {@code 400 (Bad Request)} if the annexe is not valid,
     * or with status {@code 404 (Not Found)} if the annexe is not found,
     * or with status {@code 500 (Internal Server Error)} if the annexe couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/annexes/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Annexe> partialUpdateAnnexe(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody Annexe annexe
    ) throws URISyntaxException {
        log.debug("REST request to partial update Annexe partially : {}, {}", id, annexe);
        if (annexe.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, annexe.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!annexeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Annexe> result = annexeRepository
            .findById(annexe.getId())
            .map(
                existingAnnexe -> {
                    if (annexe.getNomAnnexe() != null) {
                        existingAnnexe.setNomAnnexe(annexe.getNomAnnexe());
                    }
                    if (annexe.getEnteteAnnexe() != null) {
                        existingAnnexe.setEnteteAnnexe(annexe.getEnteteAnnexe());
                    }

                    return existingAnnexe;
                }
            )
            .map(annexeRepository::save);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, annexe.getId()));
    }

    /**
     * {@code GET  /annexes} : get all the annexes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of annexes in body.
     */
    @GetMapping("/annexes")
    public List<Annexe> getAllAnnexes() {
        log.debug("REST request to get all Annexes");
        /*  List<Annexe> list = annexeRepository.findAll();
        List<Dossier> list2 = dossierRepository.findAll();
        for (Annexe ann : list) {
            log.debug("haha" + ann.getAttributs());
        }
        for (Dossier doss : list2) {
            log.debug("DOSSIER DOSSIER DOSSIER" + doss.getProduit() + doss.getClient());
        }
		*/
        return annexeRepository.findAll();
    }

    /**
     * {@code GET  /annexes/:id} : get the "id" annexe.
     *
     * @param id the id of the annexe to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the annexe, or with status {@code 404 (Not Found)}.
     */

    @GetMapping("/annexes/detail/{id}")
    public List<Attribut> getDetailAnnexe(@PathVariable String id) {
        log.debug("REST request to get Annexe : {}", id);
        generateAnnexe(id);
        List<Attribut> attributs = attributRepository.findAllById(id);
        return attributs;
    }

    @GetMapping("/annexes/{id}")
    public ResponseEntity<Annexe> getAnnexe(@PathVariable String id) {
        log.debug("REST request to get Annexe : {}", id);
        generateAnnexe(id);
        Optional<Annexe> annexe = annexeRepository.findById(id);
        //       //Annexe annexe2 = annexeRepository.FindById(id);
        //        // MOD
        //        Annexe ann = annexe.get();
        //          List<String> attribut = new ArrayList<String>();
        //
        //          List<Attribut> attributs = attributRepository.findAllById(id);
        //
        //          for (Attribut a : attributs )
        //          {
        //            String nomAttribut= a.getNomAttribut();
        //                    attribut.add(nomAttribut);
        //                    List<AttributClient> attributsClients = attributClientRepository.findAllByNomAttribut(nomAttribut);
        //                    log.debug("tttttttttttttttttttttttttttttt"+attributsClients);
        //          }
        //
        //          log.debug("[Attribut dans le schema d'annexe] ======================> "+attribut);
        //
        //          List<Client> clients = clientRepository.findAll();
        //
        //        for(Client client : clients) {
        //            Set<AttributClient> ligneclient=  new HashSet<>();
        //
        //            List<AttributClient> attributsclients1=attributClientRepository.findAllById(client.getId());
        //            log.debug("Attributs du clinet ====>"+attributsclients1);
        //          for(String attClt : attribut ) {
        //            for(AttributClient att : attributsclients1)
        //            {
        //                if(att.getNomAttribut().equals(attClt))
        //                              {ligneclient.add(att);
        //                    log.debug("hhhhhhhhhhhhh"+att.getContenu());}
        //
        //                }
        ////              List<AttributClient> attributsclients = attributClientRepository.findAllByNomAttribut(attClt);
        ////              log.debug("########## liste des attributs pour 1 client"+attributsclients);
        //
        //          }
        //          AnnexeOut annexeout=new AnnexeOut(ann.getNomAnnexe(),client.getId(),ligneclient);
        //          AnnexeOut temp = annexeoutRepository.save(annexeout);
        //          log.debug("ligne$$$$$$$$"+ligneclient);
        //
        //        }
        ////          log.debug("attribut lié a l'annexe ======================> "+attributs);

        return ResponseUtil.wrapOrNotFound(annexe);
    }

    public static void generateAnnexe(Annexe ann, Produit prod) {
        List<String> attribut = new ArrayList<String>();

        //attribut contient les nomAttribut
        List<Attribut> attributs = attributRepository.findAllById(ann.getId());

        for (Attribut a : attributs) {
            String nomAttribut = a.getNomAttribut();
            attribut.add(nomAttribut);
            //                    List<AttributClient> attributsClients = attributClientRepository.findAllByNomAttribut(nomAttribut);
            //                    log.debug("tttttttttttttttttttttttttttttt"+attributsClients);
        }

        log.debug("[Attribut dans le schema d'annexe] ======================> " + attribut);
        List<Dossier> dossiers = dossierRepository.findAllById(prod.getId());
        List<Client> clients = new ArrayList<>();

        for (Dossier doss : dossiers) {
            /* log.debug("DOSSIER ===> " + doss.getId() + "produit" + prod.getId() + "client" + doss.getClient()); */
            clients.add(doss.getClient());
        }
        //        List<Client> clients = clientRepository.findAll();

        for (Client client : clients) {
            Set<AttributClient> ligneclient = new HashSet<>();

            List<AttributClient> attributsclients1 = attributClientRepository.findAllById(client.getId());
            log.debug("Attributs du client ====>" + attributsclients1);
            if (!attributsclients1.isEmpty()) {
                for (String attClt : attribut) {
                    for (AttributClient att : attributsclients1) {
                        if (att.getNomAttribut().equals(attClt)) {
                            ligneclient.add(att);
                            /*   log.debug("[[attributClient correspondant au schema]]" + att.getContenu()); */
                        }
                    }
                }
                log.debug("1111111111111111111111111111111111111111111111111111111");

                //            List<AttributClient> attributsclients = attributClientRepository.findAllByNomAttribut(attClt);
                //            log.debug("########## liste des attributs pour 1 client"+attributsclients);
                if (!ligneclient.isEmpty()) {
                    String nomAnnexe = ann.getNomAnnexe();
                    log.debug("2222222222222222222222222222222222222222222222222222222");
                    AnnexeGroup annGrp = annexeGroupRepository.findByNomAnnexe(nomAnnexe);
                    if (annGrp == null) {
                        annGrp = new AnnexeGroup(nomAnnexe, prod);
                    }
                    log.debug("33333333333333333333333333333333333333333333333333333333");
                    Set<AnnexeGroup> list = prod.getAnnexegroups();
                    list.add(annGrp);
                    prod.setAnnexegroups(list);
                    /*	log.debug("4444444444444444444444444444444444444prod ====>{}annGrp ====>{}",annGrp,prod);*/
                    annexeGroupRepository.save(annGrp);

                    produitRepository.save(prod);
                    log.debug("55555555555555555555555555555555555555555555555555555");

                    log.debug("PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP");
                    AnnexeOut annexeout = new AnnexeOut(nomAnnexe, client.getId(), ligneclient, annGrp);

                    Set<AnnexeOut> liste = annGrp.getAnnexeouts();
                    liste.add(annexeout);
                    annGrp.setAnnexeouts(liste);
                    AnnexeOut temp = annexeoutRepository.save(annexeout);

                    /*  log.debug("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOO {}",annexeout); */
                    AnnexeGroup temp1 = annexeGroupRepository.save(annGrp);
                    //                    AnnexeOut temp = annexeoutRepository.save(annexeout);
                }
            }

            log.debug("ligne$$$$$$$$" + ligneclient);
        }
    }

    public static void generateAnnexe(String id) {
        Optional<Annexe> annexe = annexeRepository.findById(id);
        //Annexe annexe2 = annexeRepository.FindById(id);
        // MOD
        Annexe ann = annexe.get();
        List<String> attribut = new ArrayList<String>();

        //attribut contient les nomAttribut
        List<Attribut> attributs = attributRepository.findAllById(id);

        for (Attribut a : attributs) {
            String nomAttribut = a.getNomAttribut();
            attribut.add(nomAttribut);
            List<AttributClient> attributsClients = attributClientRepository.findAllByNomAttribut(nomAttribut);
            log.debug("tttttttttttttttttttttttttttttt" + attributsClients);
        }

        log.debug("[Attribut dans le schema d'annexe] ======================> " + attribut);
        List<Client> clients = clientRepository.findAll();

        for (Client client : clients) {
            Set<AttributClient> ligneclient = new HashSet<>();

            List<AttributClient> attributsclients1 = attributClientRepository.findAllById(client.getId());
            log.debug("Attributs du clinet ====>" + attributsclients1);
            for (String attClt : attribut) {
                for (AttributClient att : attributsclients1) {
                    if (att.getNomAttribut().equals(attClt)) {
                        ligneclient.add(att);
                        log.debug("hhhhhhhhhhhhh" + att.getContenu());
                    }
                }
                //            List<AttributClient> attributsclients = attributClientRepository.findAllByNomAttribut(attClt);
                //            log.debug("########## liste des attributs pour 1 client"+attributsclients);

            }
            AnnexeOut annexeout = new AnnexeOut(ann.getNomAnnexe(), client.getId(), ligneclient);
            AnnexeOut temp = annexeoutRepository.save(annexeout);
            log.debug("ligne$$$$$$$$" + ligneclient);
        }
    }

    /**
     * {@code DELETE  /annexes/:id} : delete the "id" annexe.
     *
     * @param id the id of the annexe to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/annexes/{id}")
    public ResponseEntity<Void> deleteAnnexe(@PathVariable String id) {
        log.debug("REST request to delete Annexe : {}", id);
        annexeRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
