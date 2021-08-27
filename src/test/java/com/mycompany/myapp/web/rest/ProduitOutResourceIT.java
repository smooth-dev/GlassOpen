package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ProduitOut;
import com.mycompany.myapp.repository.ProduitOutRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link ProduitOutResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProduitOutResourceIT {

    private static final String DEFAULT_NOM_ANNEXE = "AAAAAAAAAA";
    private static final String UPDATED_NOM_ANNEXE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/produit-outs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ProduitOutRepository produitOutRepository;

    @Autowired
    private MockMvc restProduitOutMockMvc;

    private ProduitOut produitOut;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProduitOut createEntity() {
        ProduitOut produitOut = new ProduitOut().nomAnnexe(DEFAULT_NOM_ANNEXE);
        return produitOut;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProduitOut createUpdatedEntity() {
        ProduitOut produitOut = new ProduitOut().nomAnnexe(UPDATED_NOM_ANNEXE);
        return produitOut;
    }

    @BeforeEach
    public void initTest() {
        produitOutRepository.deleteAll();
        produitOut = createEntity();
    }

    @Test
    void createProduitOut() throws Exception {
        int databaseSizeBeforeCreate = produitOutRepository.findAll().size();
        // Create the ProduitOut
        restProduitOutMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(produitOut)))
            .andExpect(status().isCreated());

        // Validate the ProduitOut in the database
        List<ProduitOut> produitOutList = produitOutRepository.findAll();
        assertThat(produitOutList).hasSize(databaseSizeBeforeCreate + 1);
        ProduitOut testProduitOut = produitOutList.get(produitOutList.size() - 1);
        assertThat(testProduitOut.getNomAnnexe()).isEqualTo(DEFAULT_NOM_ANNEXE);
    }

    @Test
    void createProduitOutWithExistingId() throws Exception {
        // Create the ProduitOut with an existing ID
        produitOut.setId("existing_id");

        int databaseSizeBeforeCreate = produitOutRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProduitOutMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(produitOut)))
            .andExpect(status().isBadRequest());

        // Validate the ProduitOut in the database
        List<ProduitOut> produitOutList = produitOutRepository.findAll();
        assertThat(produitOutList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNomAnnexeIsRequired() throws Exception {
        int databaseSizeBeforeTest = produitOutRepository.findAll().size();
        // set the field null
        produitOut.setNomAnnexe(null);

        // Create the ProduitOut, which fails.

        restProduitOutMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(produitOut)))
            .andExpect(status().isBadRequest());

        List<ProduitOut> produitOutList = produitOutRepository.findAll();
        assertThat(produitOutList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllProduitOuts() throws Exception {
        // Initialize the database
        produitOutRepository.save(produitOut);

        // Get all the produitOutList
        restProduitOutMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(produitOut.getId())))
            .andExpect(jsonPath("$.[*].nomAnnexe").value(hasItem(DEFAULT_NOM_ANNEXE)));
    }

    @Test
    void getProduitOut() throws Exception {
        // Initialize the database
        produitOutRepository.save(produitOut);

        // Get the produitOut
        restProduitOutMockMvc
            .perform(get(ENTITY_API_URL_ID, produitOut.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(produitOut.getId()))
            .andExpect(jsonPath("$.nomAnnexe").value(DEFAULT_NOM_ANNEXE));
    }

    @Test
    void getNonExistingProduitOut() throws Exception {
        // Get the produitOut
        restProduitOutMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewProduitOut() throws Exception {
        // Initialize the database
        produitOutRepository.save(produitOut);

        int databaseSizeBeforeUpdate = produitOutRepository.findAll().size();

        // Update the produitOut
        ProduitOut updatedProduitOut = produitOutRepository.findById(produitOut.getId()).get();
        updatedProduitOut.nomAnnexe(UPDATED_NOM_ANNEXE);

        restProduitOutMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProduitOut.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProduitOut))
            )
            .andExpect(status().isOk());

        // Validate the ProduitOut in the database
        List<ProduitOut> produitOutList = produitOutRepository.findAll();
        assertThat(produitOutList).hasSize(databaseSizeBeforeUpdate);
        ProduitOut testProduitOut = produitOutList.get(produitOutList.size() - 1);
        assertThat(testProduitOut.getNomAnnexe()).isEqualTo(UPDATED_NOM_ANNEXE);
    }

    @Test
    void putNonExistingProduitOut() throws Exception {
        int databaseSizeBeforeUpdate = produitOutRepository.findAll().size();
        produitOut.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProduitOutMockMvc
            .perform(
                put(ENTITY_API_URL_ID, produitOut.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(produitOut))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProduitOut in the database
        List<ProduitOut> produitOutList = produitOutRepository.findAll();
        assertThat(produitOutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchProduitOut() throws Exception {
        int databaseSizeBeforeUpdate = produitOutRepository.findAll().size();
        produitOut.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProduitOutMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(produitOut))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProduitOut in the database
        List<ProduitOut> produitOutList = produitOutRepository.findAll();
        assertThat(produitOutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamProduitOut() throws Exception {
        int databaseSizeBeforeUpdate = produitOutRepository.findAll().size();
        produitOut.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProduitOutMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(produitOut)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProduitOut in the database
        List<ProduitOut> produitOutList = produitOutRepository.findAll();
        assertThat(produitOutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateProduitOutWithPatch() throws Exception {
        // Initialize the database
        produitOutRepository.save(produitOut);

        int databaseSizeBeforeUpdate = produitOutRepository.findAll().size();

        // Update the produitOut using partial update
        ProduitOut partialUpdatedProduitOut = new ProduitOut();
        partialUpdatedProduitOut.setId(produitOut.getId());

        partialUpdatedProduitOut.nomAnnexe(UPDATED_NOM_ANNEXE);

        restProduitOutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProduitOut.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProduitOut))
            )
            .andExpect(status().isOk());

        // Validate the ProduitOut in the database
        List<ProduitOut> produitOutList = produitOutRepository.findAll();
        assertThat(produitOutList).hasSize(databaseSizeBeforeUpdate);
        ProduitOut testProduitOut = produitOutList.get(produitOutList.size() - 1);
        assertThat(testProduitOut.getNomAnnexe()).isEqualTo(UPDATED_NOM_ANNEXE);
    }

    @Test
    void fullUpdateProduitOutWithPatch() throws Exception {
        // Initialize the database
        produitOutRepository.save(produitOut);

        int databaseSizeBeforeUpdate = produitOutRepository.findAll().size();

        // Update the produitOut using partial update
        ProduitOut partialUpdatedProduitOut = new ProduitOut();
        partialUpdatedProduitOut.setId(produitOut.getId());

        partialUpdatedProduitOut.nomAnnexe(UPDATED_NOM_ANNEXE);

        restProduitOutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProduitOut.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProduitOut))
            )
            .andExpect(status().isOk());

        // Validate the ProduitOut in the database
        List<ProduitOut> produitOutList = produitOutRepository.findAll();
        assertThat(produitOutList).hasSize(databaseSizeBeforeUpdate);
        ProduitOut testProduitOut = produitOutList.get(produitOutList.size() - 1);
        assertThat(testProduitOut.getNomAnnexe()).isEqualTo(UPDATED_NOM_ANNEXE);
    }

    @Test
    void patchNonExistingProduitOut() throws Exception {
        int databaseSizeBeforeUpdate = produitOutRepository.findAll().size();
        produitOut.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProduitOutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, produitOut.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(produitOut))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProduitOut in the database
        List<ProduitOut> produitOutList = produitOutRepository.findAll();
        assertThat(produitOutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchProduitOut() throws Exception {
        int databaseSizeBeforeUpdate = produitOutRepository.findAll().size();
        produitOut.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProduitOutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(produitOut))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProduitOut in the database
        List<ProduitOut> produitOutList = produitOutRepository.findAll();
        assertThat(produitOutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamProduitOut() throws Exception {
        int databaseSizeBeforeUpdate = produitOutRepository.findAll().size();
        produitOut.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProduitOutMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(produitOut))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProduitOut in the database
        List<ProduitOut> produitOutList = produitOutRepository.findAll();
        assertThat(produitOutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteProduitOut() throws Exception {
        // Initialize the database
        produitOutRepository.save(produitOut);

        int databaseSizeBeforeDelete = produitOutRepository.findAll().size();

        // Delete the produitOut
        restProduitOutMockMvc
            .perform(delete(ENTITY_API_URL_ID, produitOut.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProduitOut> produitOutList = produitOutRepository.findAll();
        assertThat(produitOutList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
